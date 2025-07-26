package com.jajaja.domain.coupon.dto;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Builder
public record CouponConditionDto(
		ConditionType type,
		List<String> values,
		Integer minOrderAmount,
		String expireAt
) {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static CouponConditionDto from(Coupon coupon) {
		List<String> valuesList = coupon.getConditionValues() != null && !coupon.getConditionValues().isEmpty()
				? Arrays.asList(coupon.getConditionValues().split(","))
				: List.of();
		
		return CouponConditionDto.builder()
				.type(coupon.getConditionType())
				.values(valuesList)
				.minOrderAmount(coupon.getMinOrderAmount())
				.expireAt(coupon.getExpiresAt().format(FORMATTER))
				.build();
	}
}
