package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.repository.CartCommandRepository;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.repository.UserRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CartHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartComponent {
	
	private final CartCommandRepository cartRepository;
	private final UserRepository userRepository;
	
	/**
	 * 사용자의 장바구니를 조회하거나 생성합니다.
	 * @param memberId 사용자 ID
	 * @return Cart
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Cart findCart(Long memberId) {
		return cartRepository.findByMemberId(memberId)
				.orElseGet(() -> createCart(memberId));
	}
	
	/**
	 * 신규 사용자를 위해 장바구니를 생성합니다.
	 * @param memberId 사용자 ID
	 * @return Cart
	 */
	private Cart createCart(Long memberId) {
		log.info("[CartComponent] 사용자 {}의 장바구니가 없어 새로 생성합니다.", memberId);
		User user = userRepository.findById(memberId)
				.orElseThrow(() -> new CartHandler(ErrorStatus.USER_NOT_FOUND));
		Cart newCart = Cart.builder().member(user).build();
		return cartRepository.save(newCart);
	}
}