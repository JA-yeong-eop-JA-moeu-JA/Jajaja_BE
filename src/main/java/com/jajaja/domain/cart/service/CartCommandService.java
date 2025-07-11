package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
public interface CartCommandService {
	public void addOrUpdateCartProduct(Long memberId, CartProductAddRequestDto request);
}
