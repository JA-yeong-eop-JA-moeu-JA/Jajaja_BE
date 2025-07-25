package com.jajaja.domain.coupon.dto;

import lombok.Builder;

@Builder
public record CouponApplyResponseDto(
        Long cartId,
        Long couponId,
        String couponName,
        Integer originalAmount,
        Integer discountAmount,
        Integer finalAmount
) {
    public static CouponApplyResponseDto of(Long cartId, Long couponId, String couponName) {
        return CouponApplyResponseDto.builder()
                .cartId(cartId)
                .couponId(couponId)
                .couponName(couponName)
                .build();
    }

    public static CouponApplyResponseDto withDiscount(Long cartId, Long couponId, String couponName, DiscountResultDto discountResult) {
        return CouponApplyResponseDto.builder()
                .cartId(cartId)
                .couponId(couponId)
                .couponName(couponName)
                .originalAmount(discountResult.originalAmount())
                .discountAmount(discountResult.discountAmount())
                .finalAmount(discountResult.finalAmount())
                .build();
    }
}