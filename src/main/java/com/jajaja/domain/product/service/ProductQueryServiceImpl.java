package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;
import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.BusinessCategoryRepository;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.repository.ProductSalesRepository;
import com.jajaja.domain.product.util.ProductPriceCalculator;
import com.jajaja.domain.review.converter.ReviewConverter;
import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.domain.team.dto.response.TeamResponseDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.entity.UserBusinessCategory;
import com.jajaja.domain.user.repository.UserBusinessCategoryRepository;
import com.jajaja.domain.user.repository.UserRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final TeamRepository teamRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final UserBusinessCategoryRepository userBusinessCategoryRepository;
    private final UserRepository userRepository;
    private final ProductSalesRepository productSalesRepository;

    @Override
    public ProductDetailResponseDto getProductDetail(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        // 모집 중인 팀 조회
        List<Team> matchingTeams = teamRepository.findMatchingTeamsByProductId(productId);

        List<TeamResponseDto> teamResponseDtoList = matchingTeams.stream()
                .map(TeamResponseDto::from)
                .toList();

        // 좋아요 수 상위 3개 리뷰만 조회
        List<Review> topReviews = reviewRepository.findTop3ByProductIdOrderByLikeCountDesc(productId);

        // 회원/비회원 경우 나눠서, review 조회
        List<ReviewResponseDto> reviewResponseDtoList = topReviews.stream()
                .map(review -> {
                    boolean isLike = false;
                    if (userId != null) {
                        isLike = review.getReviewLikes().stream()
                                .anyMatch(like -> like.getMember().getId().equals(userId));
                    }
                    return ReviewConverter.toReviewResponseDto(review, isLike);
                })
                .toList();

        int salePrice = ProductPriceCalculator.calculateDiscountedPrice(
                product.getPrice(),
                product.getDiscountRate()
        );

        double averageRating = calculateAverageRating(product.getReviews());

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
                .map(ps -> convertToDto(ps.getProduct()))
                .collect(Collectors.toList());

        // 인기 상품: 전체 상품 중 판매량 많은 순 상위 8개
        List<ProductListResponseDto> popularProducts = productSalesRepository
                .findTopProductsByTotalSales(PageRequest.of(0, 8))
                .stream()
                .map(dto -> convertToDto(dto.product()))
                .toList();

        // 신상품: 전체 상품 중 생성일 최신 순 상위 8개
        List<ProductListResponseDto> newProducts = productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .limit(8)
                .map(this::convertToDto)
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
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));
            UserBusinessCategory userBusinessCategory = userBusinessCategoryRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.USER_BUSINESS_CATEGORY_NOT_FOUND));
            return userBusinessCategory.getBusinessCategory().getId();
        }
        if (categoryId != null) {
            BusinessCategory category = businessCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BadRequestException(ErrorStatus.BUSINESS_CATEGORY_NOT_FOUND));
            return category.getId();
        }
        throw new BadRequestException(ErrorStatus.BUSINESS_CATEGORY_REQUIRED);
    }

    /**
     * 상품 엔티티를 상품 리스트 응답 DTO로 변환
     *
     * @param product 상품 엔티티
     * @return 상품 리스트 응답 DTO
     */
    private ProductListResponseDto convertToDto(Product product) {
        double rating = calculateAverageRating(product.getReviews());
        int reviewCount = product.getReviews().size();
        return ProductListResponseDto.of(product, rating, reviewCount);
    }

    /**
     * 리뷰의 별점의 평균을 계산하는 메소드
     * @paramname reviews 리뷰 리스트
     * @return 상품의 평균 리뷰 별점
     */
    private double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(review -> review.getRating())
                .average()
                .orElse(0.0);
    }
}
