package com.jajaja.domain.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CouponApplyRequestDto(
        @NotNull(message = "쿠폰 ID는 필수입니다")
        Long couponId
) {
}