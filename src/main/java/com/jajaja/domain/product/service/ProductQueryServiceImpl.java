package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.util.ProductPriceCalculator;
import com.jajaja.domain.review.converter.ReviewConverter;
import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.domain.team.dto.response.TeamResponseDto;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ProductDetailResponseDto getProductDetail(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        // 모집 중인 팀 조회
        List<TeamResponseDto> teamResponseDtoList = product.getTeams().stream()
                .filter(team ->
                        team.getStatus() == TeamStatus.MATCHING &&
                                team.getLeader() != null &&
                                team.getTeamMembers().isEmpty()
                )
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
     * 할인율 적용한 할인 가격 계산하는 메소드
     * @paramname product 상품
     * @return 할인값
     */
    private int calculateSalePrice(Product product) {
        if (product.getDiscountRate() == null) {
            return product.getPrice();
        }
        return (int) (product.getPrice() * (1 - product.getDiscountRate() / 100.0));    }

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
