package com.jajaja.domain.coupon.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CouponApplyRequestDto(
    @NotNull(message = "쿠폰 ID는 필수입니다.")
    Long couponId,
    
    @NotEmpty(message = "적용할 장바구니 상품 목록은 필수입니다.")
    List<Long> items
) {}