package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartProductDeleteRequestDto;

public interface CartCommandService {
	public void addOrUpdateCartProduct(Long memberId, CartProductAddRequestDto request);
	public void deleteCartProduct(Long memberId, CartProductDeleteRequestDto request);
}
