package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.repository.CartRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CartHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartComponent {
	
	private final CartRepository cartRepository;
	
	/**
	 * 사용자의 장바구니를 조회하거나 생성합니다.
	 *
	 * @param memberId 사용자 ID
	 * @return Cart
	 */
	public Cart findCart(Long memberId) {
		return cartRepository.findByMemberId(memberId)
				.orElseThrow(() -> new CartHandler(ErrorStatus.CART_NOT_FOUND));
	}
}