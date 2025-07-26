package com.jajaja.domain.cart.dto;

import com.jajaja.domain.coupon.dto.CouponResponseDto;
import com.jajaja.global.common.dto.PriceInfoDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CartResponseDto(
		List<CartProductResponseDto> products,
		CouponResponseDto appliedCoupon,
		PriceInfoDto summary,
		int totalCount
) {
	public static CartResponseDto of(List<CartProductResponseDto> productList, CouponResponseDto coupon, int originalAmount, int discountAmount, int shippingFee) {
		return CartResponseDto.builder()
				.products(productList)
				.appliedCoupon(coupon)
				.summary(PriceInfoDto.of(originalAmount, discountAmount, shippingFee))
				.totalCount(productList.size())
				.build();
	}
}