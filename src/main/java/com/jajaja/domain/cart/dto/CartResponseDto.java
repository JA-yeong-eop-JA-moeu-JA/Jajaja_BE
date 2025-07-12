package com.jajaja.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
import com.jajaja.domain.coupon.entity.enums.DiscountType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CartResponseDto(
		List<CartItemInfoDto> data,
		AppliedCouponInfoDto appliedCoupon,
		SummaryInfoDto summary,
		int totalCount,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
		LocalDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
		LocalDateTime updatedAt
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