package com.jajaja.domain.cart.dto;

import com.jajaja.domain.coupon.dto.CouponResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CartResponseDto(
		List<CartProductResponseDto> data,
		CouponResponseDto appliedCoupon,
		SummaryInfoDto summary,
		int totalCount
) {
	
	public record SummaryInfoDto(
			int shippingFee
	) {}
}