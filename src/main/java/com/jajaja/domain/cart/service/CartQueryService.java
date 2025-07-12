package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartResponseDto;

public interface CartQueryService {
	public CartResponseDto getCart(Long memberId);
}
