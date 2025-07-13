package com.jajaja.domain.cart.dto;

import com.jajaja.domain.coupon.entity.enums.ConditionType;
import com.jajaja.domain.coupon.entity.enums.DiscountType;
import lombok.Builder;

import java.util.List;

@Builder
public record CartResponseDto(
		List<CartItemInfoDto> data,
		AppliedCouponInfoDto appliedCoupon,
		SummaryInfoDto summary,
		int totalCount
) {
	public record CartItemInfoDto(
			Long id,
			Long productId,
			String productName,
			String brand,
			Long optionId,
			String option,
			int quantity,
			String productThumbnail,
			int unitPrice,
			int totalPrice,
			boolean teamAvailable
	) {}
	
	public record AppliedCouponInfoDto(
			Long couponId,
			String couponName,
			DiscountType discountType,
			Integer discountValue,
			CouponConditionsInfoDto applicableConditions
	) {}
	
	public record CouponConditionsInfoDto(
			ConditionType type,
			String value,
			Integer minOrderAmount
	) {}
	
	public record SummaryInfoDto(
			int shippingFee
	) {}
}