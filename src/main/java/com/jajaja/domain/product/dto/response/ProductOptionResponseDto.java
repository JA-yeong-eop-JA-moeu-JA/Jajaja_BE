package com.jajaja.domain.product.dto.response;

import lombok.Builder;

@Builder
public record ProductOptionResponseDto(
        Long id,
        String name,
        Integer originPrice,
        Integer unitPrice
) {
    public static ProductOptionResponseDto of(
            Long id,
            String name,
            Integer originPrice,
            Integer unitPrice
    ) {
        return ProductOptionResponseDto.builder()
                .id(id)
                .name(name)
                .originPrice(originPrice)
                .unitPrice(unitPrice)
                .build();
    }
}