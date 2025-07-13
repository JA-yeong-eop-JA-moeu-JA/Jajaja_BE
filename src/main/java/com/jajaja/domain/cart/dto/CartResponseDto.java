package com.jajaja.domain.cart.dto;

import com.jajaja.domain.coupon.dto.AppliedCouponResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CartResponseDto(
		List<CartProductResponseDto> data,
		AppliedCouponResponseDto appliedCoupon,
		SummaryInfoDto summary,
		int totalCount
) {
	
	public record SummaryInfoDto(
			int shippingFee
	) {}
}