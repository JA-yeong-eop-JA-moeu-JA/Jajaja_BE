package com.jajaja.domain.cart.service;

import com.jajaja.converter.CartConverter;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.repository.CartCommandRepository;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamCommandRepository;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.repository.UserRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CartHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
	private final CartCommandRepository cartRepository;
	private final TeamCommandRepository teamRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public CartResponseDto getCart(Long memberId) {
		log.info("[CartService] 사용자 {}의 장바구니 조회 시작", memberId);
		Cart cart = cartRepository.findByMemberId(memberId)
				.orElseGet(() -> {
					log.info("[CartService] 사용자 {}의 장바구니가 없어 새로 생성합니다.", memberId);
					return createNewCart(memberId);
				});
		
		if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
			log.warn("[CartService] 사용자 {}의 장바구니가 비어있습니다.", memberId);
			return CartConverter.toEmptyCartResponseDto(cart);
		}
		
		List<CartResponseDto.CartItemInfoDto> itemInfos = cart.getCartProducts().stream()
				.map(cartProduct -> {
					boolean isTeamAvailable = teamRepository.existsByProductIdAndStatus(cartProduct.getProduct().getId(), TeamStatus.MATCHING);
					return CartConverter.toCartItemInfoDto(cartProduct, isTeamAvailable);
				})
				.collect(Collectors.toList());
		
		log.info("[CartService] 사용자 {}의 장바구니 {}, 아이템 개수 {}개 조회 완료", memberId, cart.getId(), itemInfos.size());
		return CartConverter.toCartResponseDto(cart, itemInfos);
	}
	
	private Cart createNewCart(Long memberId) {
		User user = userRepository.findById(memberId)
				.orElseThrow(() -> new CartHandler(ErrorStatus.USER_NOT_FOUND));
		Cart newCart = Cart.builder().member(user).build();
		return cartRepository.save(newCart);
	}
}