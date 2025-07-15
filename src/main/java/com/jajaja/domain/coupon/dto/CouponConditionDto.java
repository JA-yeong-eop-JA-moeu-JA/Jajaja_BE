package com.jajaja.domain.coupon.dto;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
import lombok.Builder;

@Builder
public record CouponConditionDto(
		ConditionType type,
		String value,
		Integer minOrderAmount
) {
	public static CouponConditionDto from(Coupon coupon) {
		if(coupon == null) return
			CouponConditionDto.builder()
					.build();
		
		
		return CouponConditionDto.builder()
				.type(coupon.getConditionType())
				.value(coupon.getConditionValues())
				.minOrderAmount(coupon.getMinOrderAmount())
				.build();
	}
}
