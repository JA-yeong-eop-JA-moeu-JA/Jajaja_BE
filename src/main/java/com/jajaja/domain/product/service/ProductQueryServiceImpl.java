package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.util.ProductPriceCalculator;
import com.jajaja.domain.review.converter.ReviewConverter;
import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.team.converter.TeamConverter;
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

    @Override
    public ProductDetailResponseDto getProductDetail(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        List<TeamResponseDto> teamResponseDtoList = product.getTeams().stream()
                .filter(team ->
                        team.getStatus() == TeamStatus.MATCHING &&
                                team.getLeader() != null &&
                                team.getTeamMembers().isEmpty()
                )
                .map(TeamConverter::toTeamResponseDto)
                .toList();

        List<ReviewResponseDto> reviewResponseDtoList = product.getReviews().stream()
                .sorted((r1, r2) -> Integer.compare(
                        r2.getReviewLikes().size(),
                        r1.getReviewLikes().size()
                ))
                .limit(3)
                .map(review -> ReviewConverter.toReviewResponseDto(review, userId))
                .toList();

        int salePrice = ProductPriceCalculator.calculateDiscountedPrice(product.getPrice(), product.getDiscountRate());
        double averageRating = calculateAverageRating(product.getReviews());

        return ProductDetailResponseDto.of(
                product.getThumbnailUrl(),
                product.getStore(),
                product.getName(),
                product.getPrice(),
                salePrice,
                product.getDiscountRate(),
                product.getImageUrl(),
                averageRating,
                product.getReviews().size(),
                product.getDeliveryPeriod(),
                teamResponseDtoList,
                reviewResponseDtoList
        );
    };

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
