package com.jajaja.domain.product.dto.response;

import com.jajaja.domain.product.entity.Product;
import lombok.Builder;

@Builder
public record ProductListResponseDto(
        Long id,
        String name,
        String store,
        int price,
        String imageUrl,
        int discountRate,
        double rating,
        int reviewCount
) {
    public static ProductListResponseDto of(Product product, double rating, int reviewCount) {
        return ProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .store(product.getStore())
                .price(product.getPrice())
                .imageUrl(product.getThumbnailUrl())
                .discountRate(product.getDiscountRate() != null ? product.getDiscountRate() : 0)
                .rating(rating)
                .reviewCount(reviewCount)
                .build();
    }
}
