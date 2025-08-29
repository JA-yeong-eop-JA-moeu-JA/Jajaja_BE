package com.jajaja.domain.coupon.service;

import com.jajaja.domain.coupon.dto.CouponApplyRequestDto;
import com.jajaja.domain.coupon.dto.CouponApplyResponseDto;

public interface CouponCommandService {
    CouponApplyResponseDto applyCouponToCart(Long memberId, CouponApplyRequestDto request);
    void unapplyCoupon(Long memberId);
}
