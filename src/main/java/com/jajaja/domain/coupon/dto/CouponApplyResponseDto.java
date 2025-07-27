package com.jajaja.domain.coupon.dto;

import com.jajaja.global.common.dto.PriceInfoDto;
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

    public static CouponApplyResponseDto withDiscount(Long cartId, Long couponId, String couponName, PriceInfoDto priceInfo) {
        return CouponApplyResponseDto.builder()
                .cartId(cartId)
                .couponId(couponId)
                .couponName(couponName)
                .originalAmount(priceInfo.originalAmount())
                .discountAmount(priceInfo.discountAmount())
                .finalAmount(priceInfo.finalAmount())
                .build();
    }
}