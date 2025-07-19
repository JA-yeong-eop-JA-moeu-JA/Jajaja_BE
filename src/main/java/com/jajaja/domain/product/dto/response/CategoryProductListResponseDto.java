package com.jajaja.domain.product.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;

import java.util.List;

public record CategoryProductListResponseDto(
        PageResponse page,
        List<ProductItemDto> products
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
