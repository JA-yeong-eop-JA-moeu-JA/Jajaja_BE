package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.converter.CartConverter;
import com.jajaja.domain.cart.dto.CartProductResponseDto;
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
@Transactional(readOnly = true)
public class CartQueryServiceImpl implements CartQueryService {
	
	private final CartCommonService cartCommonService;
	private final TeamCommandRepository teamRepository;
	
	@Override
	public CartResponseDto getCart(Long memberId) {
		log.info("[CartQueryService] 사용자 {}의 장바구니 조회 시작", memberId);
		
		// 이미 생성된 장바구니가 있는지 확인, 없다면 생성
		Cart cart = cartCommonService.findCart(memberId);
		
		// 장바구니가 비어있는 경우 emptyDto 반환
		if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
			log.warn("[CartQueryService] 사용자 {}의 장바구니가 비어있습니다.", memberId);
			return CartConverter.toEmptyCartResponseDto();
		}
		
		// 장바구니 내 아이템에 대해 팀 참여 가능 여부 확인, 매핑
		List<CartProductResponseDto> itemInfos = cart.getCartProducts().stream()
				.map(cartProduct -> {
					boolean isTeamAvailable = teamRepository.existsByProductIdAndStatus(cartProduct.getProduct().getId(), TeamStatus.MATCHING);
					return CartProductResponseDto.of(cartProduct, isTeamAvailable);
				})
				.collect(Collectors.toList());
		
		log.info("[CartQueryService] 사용자 {}의 장바구니 {}, 아이템 개수 {}개 조회 완료", memberId, cart.getId(), itemInfos.size());
		return CartConverter.toCartResponseDto(cart, itemInfos);
	}
}