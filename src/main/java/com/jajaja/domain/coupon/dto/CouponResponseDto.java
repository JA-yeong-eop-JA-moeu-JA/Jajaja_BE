package com.jajaja.domain.coupon.dto;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.DiscountType;
import lombok.Builder;

@Builder
public record CouponResponseDto(
		Long couponId,
		String couponName,
		DiscountType discountType,
		Integer discountValue,
		CouponConditionDto applicableConditions
) {
	public static CouponResponseDto from (Coupon coupon) {
		
		CouponConditionDto applicableConditions = CouponConditionDto.from(coupon);
		
		return CouponResponseDto.builder()
				.couponId(coupon.getId())
				.couponName(coupon.getName())
				.discountType(coupon.getDiscountType())
				.discountValue(coupon.getDiscountValue())
				.applicableConditions(applicableConditions)
				.build();
	}
}