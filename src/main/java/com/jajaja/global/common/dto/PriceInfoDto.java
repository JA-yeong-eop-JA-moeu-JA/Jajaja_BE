package com.jajaja.global.common.dto;

import lombok.Builder;

@Builder
public record PriceInfoDto(
        int originalAmount,
        int discountAmount,
        int finalAmount,
        int shippingFee
) {
    public static PriceInfoDto noDiscount(int originalAmount) {
        return PriceInfoDto.builder()
                .originalAmount(originalAmount)
                .discountAmount(0)
                .finalAmount(originalAmount)
                .shippingFee(0)
                .build();
    }
    
    public static PriceInfoDto withDiscount(int originalAmount, int discountAmount) {
        return PriceInfoDto.builder()
                .originalAmount(originalAmount)
                .discountAmount(discountAmount)
                .finalAmount(originalAmount - discountAmount)
                .shippingFee(0)
                .build();
    }
    
    public static PriceInfoDto of(int originalAmount, int discountAmount, int shippingFee) {
        return PriceInfoDto.builder()
                .originalAmount(originalAmount)
                .discountAmount(discountAmount)
                .finalAmount(originalAmount - discountAmount + shippingFee)
                .shippingFee(shippingFee)
                .build();
    }
}