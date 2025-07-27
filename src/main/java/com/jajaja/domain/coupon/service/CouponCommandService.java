package com.jajaja.domain.coupon.service;

import com.jajaja.domain.coupon.dto.CouponApplyResponseDto;

public interface CouponCommandService {
    CouponApplyResponseDto applyCouponToCart(Long memberId, Long couponId);
    void unapplyCoupon(Long memberId);
}
