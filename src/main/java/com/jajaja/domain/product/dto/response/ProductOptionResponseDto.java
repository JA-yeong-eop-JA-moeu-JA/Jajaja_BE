package com.jajaja.domain.product.dto.response;

import com.jajaja.domain.product.entity.ProductOption;
import lombok.Builder;

@Builder
public record ProductOptionResponseDto(
        Long id,
        String name,
        Integer originPrice,
        Integer unitPrice
) {
    public static ProductOptionResponseDto of(
            ProductOption productOption,
            Integer unitPrice
    ) {
        return ProductOptionResponseDto.builder()
                .id(productOption.getId())
                .name(productOption.getName())
                .originPrice(productOption.getPrice())
                .unitPrice(unitPrice)
                .build();
    }
}