package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.converter.CartConverter;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Query 서비스는 읽기 전용으로 성능을 최적화합니다.
public class CartQueryServiceImpl implements CartQueryService {
	
	private final CartComponent cartComponent;
	private final TeamCommandRepository teamRepository;
	
	@Override
	public CartResponseDto getCart(Long memberId) {
		log.info("[CartQueryService] 사용자 {}의 장바구니 조회 시작", memberId);
		
		Cart cart = cartComponent.findCart(memberId);
		
		if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
			log.warn("[CartQueryService] 사용자 {}의 장바구니가 비어있습니다.", memberId);
			return CartConverter.toEmptyCartResponseDto(cart);
		}
		
		List<CartResponseDto.CartItemInfoDto> itemInfos = cart.getCartProducts().stream()
				.map(cartProduct -> {
					boolean isTeamAvailable = teamRepository.existsByProductIdAndStatus(cartProduct.getProduct().getId(), TeamStatus.MATCHING);
					return CartConverter.toCartItemInfoDto(cartProduct, isTeamAvailable);
				})
				.collect(Collectors.toList());
		
		log.info("[CartQueryService] 사용자 {}의 장바구니 {}, 아이템 개수 {}개 조회 완료", memberId, cart.getId(), itemInfos.size());
		return CartConverter.toCartResponseDto(cart, itemInfos);
	}
}