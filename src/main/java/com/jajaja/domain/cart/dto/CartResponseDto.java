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
			int originalAmount, // 원래 금액 (할인 전)
			int discountAmount, // 할인 금액
			int finalAmount, // 최종 금액 (할인 후)
			int shippingFee // 배송비
	) {}
}