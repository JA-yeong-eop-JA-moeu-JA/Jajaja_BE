package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartResponseDto;

import java.util.List;

public interface CartCommandService {
	CartResponseDto addOrUpdateCartProduct(Long memberId, List<CartProductAddRequestDto> request);
	void deleteCartProduct(Long memberId, Long productId, Long optionId);
	void deleteCartProducts(Long memberId, List<Long> cartProductIds);
}
