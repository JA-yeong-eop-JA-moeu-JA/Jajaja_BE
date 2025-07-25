package com.jajaja.domain.coupon.dto;

import lombok.Builder;

@Builder
public record DiscountResultDto(
        int originalAmount,
        int discountAmount,
        int finalAmount
) {
    public static DiscountResultDto noDiscount(int originalAmount) {
        return DiscountResultDto.builder()
                .originalAmount(originalAmount)
                .discountAmount(0)
                .finalAmount(originalAmount)
                .build();
    }
    
    public static DiscountResultDto withDiscount(int originalAmount, int discountAmount) {
        return DiscountResultDto.builder()
                .originalAmount(originalAmount)
                .discountAmount(discountAmount)
                .finalAmount(originalAmount - discountAmount)
                .build();
    }
}