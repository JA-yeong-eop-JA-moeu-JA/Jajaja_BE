
package com.jajaja.domain.product.dto.response;

import java.util.List;

public record HomeProductListResponseDto(
        List<ProductListResponseDto> recommendProducts,
        List<ProductListResponseDto> popularProducts,
        List<ProductListResponseDto> newProducts
) {
    public static HomeProductListResponseDto of(
            List<ProductListResponseDto> recommendProducts,
            List<ProductListResponseDto> popularProducts,
            List<ProductListResponseDto> newProducts
    ) {
        return new HomeProductListResponseDto(recommendProducts, popularProducts, newProducts);
    }
}

