package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;

import java.util.List;

public interface CartCommandService {
	public void addOrUpdateCartProduct(Long memberId, List<CartProductAddRequestDto> request);
	public void deleteCartProduct(Long memberId, Long productId, Long optionId);
}
