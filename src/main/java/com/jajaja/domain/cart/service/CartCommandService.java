package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;

import java.util.List;

public interface CartCommandService {
	void addOrUpdateCartProduct(Long memberId, List<CartProductAddRequestDto> request);
	void deleteCartProduct(Long memberId, Long productId, Long optionId);
	void deleteCartProducts(Long memberId, List<Long> cartProductIds);
}
