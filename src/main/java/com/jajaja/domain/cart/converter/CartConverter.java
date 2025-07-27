package com.jajaja.domain.cart.converter;

import com.jajaja.domain.cart.dto.CartProductResponseDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.coupon.dto.CouponResponseDto;
import com.jajaja.global.common.dto.PriceInfoDto;

import java.util.Collections;
import java.util.List;

public class CartConverter {
	
	public static CartResponseDto toCartResponseDto(Cart cart, List<CartProductResponseDto> itemInfos, PriceInfoDto priceInfo) {
		CouponResponseDto coupon = cart.getCoupon() == null 
			? null 
			: CouponResponseDto.from(cart.getCoupon());
		
		return CartResponseDto.of(
			itemInfos,
			coupon,
			priceInfo.originalAmount(),
			priceInfo.discountAmount(),
			priceInfo.shippingFee()
		);
	}
	
	public static CartResponseDto toEmptyCartResponseDto() {
		return CartResponseDto.of(
			Collections.emptyList(),
			null,
			0,
			0,
			0
		);
	}
}