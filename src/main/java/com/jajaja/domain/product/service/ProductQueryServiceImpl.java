package com.jajaja.domain.product.service;

import com.jajaja.domain.product.converter.ProductConverter;
import com.jajaja.domain.product.dto.response.CategoryProductListResponseDto;
import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;
import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.enums.ProductSortType;
import com.jajaja.domain.product.repository.BusinessCategoryRepository;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.repository.ProductSalesRepository;
import com.jajaja.domain.review.dto.response.ReviewListDto;
import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.domain.review.entity.ReviewImage;
import com.jajaja.domain.review.repository.ReviewImageRepository;
import com.jajaja.domain.review.repository.ReviewLikeRepository;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.domain.team.dto.response.TeamListDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberBusinessCategory;
import com.jajaja.domain.member.repository.MemberBusinessCategoryRepository;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.PageResponse;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final TeamRepository teamRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final MemberBusinessCategoryRepository memberBusinessCategoryRepository;
    private final MemberRepository memberRepository;
    private final ProductSalesRepository productSalesRepository;
    private final ProductCommonService productCommonService;
    private final ProductConverter productConverter;

    @Override
    public ProductDetailResponseDto getProductDetail(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        // 모집 중인 팀 조회
        List<Team> matchingTeams = teamRepository.findMatchingTeamsByProductId(productId);

        List<TeamListDto> teamResponseDtoList = matchingTeams.stream()
                .map(TeamListDto::from)
                .toList();

        // 좋아요 수 상위 3개 리뷰만 조회
        List<ReviewItemDto> reviewPageDtos = reviewRepository.findTop3ItemByProductIdOrderByLikeCountDesc(productId);

        List<Integer> reviewIds = reviewPageDtos.stream()
                .map(ReviewItemDto::id)
                .toList();

        // imageUrls 조회
        Map<Integer, List<String>> imageUrlsMap = reviewImageRepository
                .findTop6ImageUrlsGroupedByReviewIds(reviewIds);

        // isLike 조회
        Set<Integer> likedReviewIds = userId != null
                ? reviewLikeRepository.findReviewIdsLikedByUser(userId, reviewIds)
                : Set.of();

        // 병합
        List<ReviewListDto> reviewResponseDtoList = reviewPageDtos.stream()
                .map(dto -> new ReviewListDto(
                        dto,
                        likedReviewIds.contains(dto.id()),
                        imageUrlsMap.getOrDefault(dto.id(), List.of())
                ))
                .toList();


        int salePrice = productCommonService.calculateDiscountedPrice(
                product.getPrice(),
                product.getDiscountRate()
        );

        double averageRating = productCommonService.calculateAverageRating(product.getReviews());

        return ProductDetailResponseDto.of(
                product,
                salePrice,
                averageRating,
                teamResponseDtoList,
                reviewResponseDtoList
        );
    }

    /**
     * 홈 화면에 표시할 상품 리스트 조회
     *
     * @param userId     회원 ID (비회원이면 null)
     * @param categoryId 비회원 업종 ID (회원이면 null)
     * @return 홈 화면 상품 리스트 DTO
     */
    @Override
    public HomeProductListResponseDto getProductList(Long userId, Long categoryId) {
        Long targetCategoryId = resolveCategoryId(userId, categoryId);

        // 추천 상품: 해당 업종 내 판매량 많은 순 상위 8개
        List<ProductListResponseDto> recommendProducts = productSalesRepository
                .findByBusinessCategoryIdOrderBySalesCountDesc(targetCategoryId)
                .stream()
                .limit(8)
                .map(ps -> productConverter.toProductListResponseDto(ps.getProduct()))
                .collect(Collectors.toList());

        // 인기 상품: 전체 상품 중 판매량 많은 순 상위 8개
        List<ProductListResponseDto> popularProducts = productSalesRepository
                .findTopProductsByTotalSales(PageRequest.of(0, 8))
                .stream()
                .map(dto -> productConverter.toProductListResponseDto(dto.product()))
                .toList();

        // 신상품: 전체 상품 중 생성일 최신 순 상위 8개
        List<ProductListResponseDto> newProducts = productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .limit(8)
                .map(productConverter::toProductListResponseDto)
                .toList();

        return HomeProductListResponseDto.of(recommendProducts, popularProducts, newProducts);
    }

    /**
     * 회원인 경우 userId로 User 엔티티 조회 후 업종 ID 찾기,
     * 비회원인 경우 categoryId로 업종 조회,
     * 둘 다 없으면 예외 발생
     *
     * @param userId     회원 ID
     * @param categoryId 비회원 업종 ID
     * @return 업종 ID
     * @throws BadRequestException 업종 정보가 없으면 발생
     */
    private Long resolveCategoryId(Long userId, Long categoryId) {
        if (userId != null) {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));
            MemberBusinessCategory memberBusinessCategory = memberBusinessCategoryRepository.findByMember(member)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.USER_BUSINESS_CATEGORY_NOT_FOUND));
            return memberBusinessCategory.getBusinessCategory().getId();
        }
        if (categoryId != null) {
            BusinessCategory category = businessCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.BUSINESS_CATEGORY_NOT_FOUND));
            return category.getId();
        }
        throw new BadRequestException(ErrorStatus.BUSINESS_CATEGORY_REQUIRED);
    }

    @Override
    public CategoryProductListResponseDto getProductsBySubCategory(Long subcategoryId, String sort, Pageable pageable) {
        ProductSortType sortType = ProductSortType.from(sort);

        List<Product> products = switch (sortType) {
            case POPULAR -> productSalesRepository.findProductsBySubCategoryOrderBySalesDesc(subcategoryId, pageable);
            case NEW -> productRepository.findBySubCategoryOrderByCreatedAtDesc(subcategoryId, pageable);
            case PRICE_ASC -> productRepository.findBySubCategoryOrderByPriceAsc(subcategoryId, pageable);
            case REVIEW -> productRepository.findBySubCategoryOrderByReviewCountDesc(subcategoryId, pageable);
        };

        boolean hasNext = products.size() > pageable.getPageSize();
        List<Product> trimmedProducts = hasNext ? products.subList(0, pageable.getPageSize()) : products;

        long totalCount = productRepository.countBySubCategoryId(subcategoryId);
        Page<Product> productPage = new PageImpl<>(trimmedProducts, pageable, totalCount);

        List<CategoryProductListResponseDto.ProductItemDto> productDtos = trimmedProducts.stream()
                .map(product -> {
                    int salePrice = productCommonService.calculateDiscountedPrice(product.getPrice(), product.getDiscountRate());
                    double rating = productCommonService.calculateAverageRating(product.getReviews());
                    int reviewCount = product.getReviews().size();

                    return CategoryProductListResponseDto.ProductItemDto.builder()
                            .productId(product.getId())
                            .name(product.getName())
                            .salePrice(salePrice)
                            .discountRate(product.getDiscountRate() != null ? product.getDiscountRate() : 0)
                            .imageUrl(product.getThumbnailUrl())
                            .store(product.getStore())
                            .rating(rating)
                            .reviewCount(reviewCount)
                            .build();
                })
                .toList();

        return new CategoryProductListResponseDto(PageResponse.from(productPage), productDtos);
    }
}
