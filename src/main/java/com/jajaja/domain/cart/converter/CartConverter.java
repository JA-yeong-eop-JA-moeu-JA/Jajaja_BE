package com.jajaja.domain.cart.converter;

import com.jajaja.domain.cart.dto.CartProductResponseDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.coupon.dto.CouponResponseDto;

import java.util.Collections;
import java.util.List;

public class CartConverter {
	
	public static CartResponseDto toCartResponseDto(Cart cart, List<CartProductResponseDto> itemInfos) {
		CouponResponseDto couponInfo = cart.getCoupon() == null ? null: CouponResponseDto.from(cart.getCoupon());
		CartResponseDto.SummaryInfoDto summaryInfoDto = toSummaryInfoDto(0); // TODO: 배송비 계산 로직 구현 필요
		
		return new CartResponseDto(
				itemInfos,
				couponInfo,
				summaryInfoDto,
				itemInfos.size()
		);
	}
	
	public static CartResponseDto toEmptyCartResponseDto() {
		return new CartResponseDto(
				Collections.emptyList(),
				null,
				new CartResponseDto.SummaryInfoDto(0),
				0
		);
	}
	
	public static CartResponseDto.SummaryInfoDto toSummaryInfoDto(int deliveryFee) {
		return new CartResponseDto.SummaryInfoDto(deliveryFee);
	}
}