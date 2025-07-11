package com.jajaja.domain.product.dto.response;

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
        return ProductDetailResponseDto.builder()
                .thumbnailUrl(thumbnailUrl)
                .store(store)
                .name(name)
                .originPrice(originPrice)
                .salePrice(salePrice)
                .discountRate(discountRate)
                .imageUrl(imageUrl)
                .rating(rating)
                .reviewCount(reviewCount)
                .deliveryPeriod(deliveryPeriod)
                .teams(teams)
                .reviews(reviews)
                .build();
    }
}