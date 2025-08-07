package com.jajaja.domain.product.dto.response;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.review.dto.response.ReviewListDto;
import com.jajaja.domain.team.dto.response.TeamListDto;
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
        Long reviewCount,
        int deliveryPeriod,
        List<TeamListDto> teams,
        List<ReviewListDto> reviews
) {
    public static ProductDetailResponseDto of(
            Product product,
            Long reviewCount,
            int salePrice,
            double averageRating,
            List<TeamListDto> teams,
            List<ReviewListDto> reviews
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
                .reviewCount(reviewCount)
                .deliveryPeriod(product.getDeliveryPeriod())
                .teams(teams)
                .reviews(reviews)
                .build();
    }
}