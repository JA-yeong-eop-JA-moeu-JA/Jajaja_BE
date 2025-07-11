package com.jajaja.domain.product.dto.response;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.team.dto.response.TeamResponseDto;
import lombok.Builder;

import java.util.List;


@Builder
public record ProductDetailResponseDto(
        String thumbnailUrl,
        String store,
        String name,
        int originPrice,
        int salePrice,
        int discountRate,
        String imageUrl,
        double rating,
        int reviewCount,
        int deliveryPeriod,
        List<TeamResponseDto> teams,
        List<ReviewResponseDto> reviews
) {
    public static ProductDetailResponseDto of(
            Product product,
            int salePrice,
            double averageRating,
            List<TeamResponseDto> teams,
            List<ReviewResponseDto> reviews
    ) {
        return ProductDetailResponseDto.builder()
                .thumbnailUrl(product.getThumbnailUrl())
                .store(product.getStore())
                .name(product.getName())
                .originPrice(product.getPrice())
                .salePrice(salePrice)
                .discountRate(product.getDiscountRate())
                .imageUrl(product.getImageUrl())
                .rating(averageRating)
                .reviewCount(product.getReviews().size())
                .deliveryPeriod(product.getDeliveryPeriod())
                .teams(teams)
                .reviews(reviews)
                .build();
    }
}