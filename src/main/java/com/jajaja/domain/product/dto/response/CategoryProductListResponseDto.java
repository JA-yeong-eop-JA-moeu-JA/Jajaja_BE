package com.jajaja.domain.product.dto.response;

import lombok.Builder;

import java.util.List;

public record CategoryProductListResponseDto(
        List<ProductItemDto> content,
        int page,
        int size,
        boolean hasNext
) {

    @Builder
    public record ProductItemDto(
            Long productId,
            String name,
            int salePrice,
            int discountRate,
            String imageUrl,
            String store,
            double rating,
            int reviewCount
    ) {}
}
