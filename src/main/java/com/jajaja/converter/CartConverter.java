package com.jajaja.converter;

import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;

import java.util.Collections;
import java.util.List;

public class CartConverter {
	
	public static CartResponseDto toCartResponseDto(Cart cart, List<CartResponseDto.CartItemInfoDto> itemInfos) {
		CartResponseDto.AppliedCouponInfoDto couponInfo = toAppliedCouponInfoDto(cart.getCoupon());
		CartResponseDto.SummaryInfoDto summaryInfoDto = toSummaryInfoDto(0); // TODO: 배송비 계산 로직 구현 필요
		
		return new CartResponseDto(
				itemInfos,
				couponInfo,
				summaryInfoDto,
				itemInfos.size(),
				cart.getCreatedAt(),
				cart.getUpdatedAt()
		);
	}
	
	public static CartResponseDto toEmptyCartResponseDto(Cart cart) {
		return new CartResponseDto(
				Collections.emptyList(),
				null,
				new CartResponseDto.SummaryInfoDto(0),
				0,
				cart.getCreatedAt(),
				cart.getUpdatedAt()
		);
	}
	
	public static CartResponseDto.CartItemInfoDto toCartItemInfoDto(CartProduct cartProduct, boolean isTeamAvailable) {
		Product product = cartProduct.getProduct();
		ProductOption option = cartProduct.getProductOption();
		
		Long optionId = (option != null) ? option.getId() : null;
		String optionName = (option != null) ? option.getName() : "";
		Integer price = (option != null) ? option.getPrice() : product.getPrice();
		
		return new CartResponseDto.CartItemInfoDto(
				cartProduct.getId(),
				product.getId(),
				product.getName(),
				product.getStore(),
				optionId,
				optionName,
				cartProduct.getQuantity(),
				product.getImageUrl(),
				price,
				cartProduct.getTotalPrice(),
				isTeamAvailable
		);
	}
	
	public static CartResponseDto.AppliedCouponInfoDto toAppliedCouponInfoDto(Coupon coupon) {
		if (coupon == null) {
			return null;
		}
		
		CartResponseDto.CouponConditionsInfoDto conditions = toCouponConditionsInfoDto(coupon);
		
		return new CartResponseDto.AppliedCouponInfoDto(
				coupon.getId(),
				coupon.getName(),
				coupon.getDiscountType(),
				coupon.getDiscountValue(),
				conditions
		);
	}
	
	public static CartResponseDto.CouponConditionsInfoDto toCouponConditionsInfoDto(Coupon coupon) {
		
		return new CartResponseDto.CouponConditionsInfoDto( // TODO: 쿠폰 적용 조건 상세 로직 구현 필요
				coupon.getConditionType(),
				coupon.getConditionValues(),
				coupon.getMinOrderAmount()
		);
	}
	
	public static CartResponseDto.SummaryInfoDto toSummaryInfoDto(int deliveryFee) {
		return new CartResponseDto.SummaryInfoDto(deliveryFee);
	}
}