package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;
import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.BusinessCategoryRepository;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.repository.ProductSalesRepository;
import com.jajaja.domain.review.entity.Review;
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
public class ProductListQueryServiceImpl implements ProductListQueryService {

    private final ProductRepository productRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final UserBusinessCategoryRepository userBusinessCategoryRepository;
    private final UserRepository userRepository;
    private final ProductSalesRepository productSalesRepository;

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
     * 리뷰 리스트에서 평균 별점 계산
     *
     * @param reviews 리뷰 리스트
     * @return 평균 별점 (리뷰가 없으면 0.0)
     */
    private double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
