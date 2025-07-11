package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.converter.CartConverter;
import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartCommandRepository;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;
import com.jajaja.domain.product.repository.ProductOptionRepository;
import com.jajaja.domain.product.repository.ProductRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
	private final CartCommandRepository cartRepository;
	private final CartProductRepository cartProductRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ProductOptionRepository productOptionRepository;
	private final TeamCommandRepository teamRepository;
	
	@Transactional
	public CartResponseDto getCart(Long memberId) {
		log.info("[CartService] 사용자 {}의 장바구니 조회 시작", memberId);
		Cart cart = findCart(memberId);
		
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
	
	@Transactional
	public void addOrUpdateCartProduct(Long memberId, CartProductAddRequestDto request) {
		log.info("[CartService] 사용자 {}의 장바구니에 아이템 {} 옵션 {} {}개 추가", memberId, request.productId(), request.optionId(), request.quantity());
		Cart cart = findCart(memberId);
		Product product = productRepository.findById(request.productId()).orElseThrow(() -> new CartHandler(ErrorStatus.PRODUCT_NOT_FOUND));
		ProductOption option;
		if(request.optionId() != null) {
			option = productOptionRepository.findById(request.optionId())
					.filter(opt -> opt.getProduct().getId().equals(product.getId()))
					.orElseThrow(() -> new CartHandler(ErrorStatus.OPTION_NOT_FOUND));
		} else {
			option = null;
		}
		
		Optional<CartProduct> existingItem = cartProductRepository.findByCartIdAndProductId(cart.getId(), product.getId());
		existingItem.ifPresentOrElse(
				item -> {
					log.info("[CartService] 기존 아이템 {}이 있어 수량을 {}개로 변경합니다.", item.getId(), request.quantity());
					item.updateQuantity(request.quantity());
					item.updateOption(option);
				},
				() -> {
					log.info("[CartService] 장바구니에 새로 아이템 {}를 {}개 추가합니다.", request.productId(), request.quantity());
					cart.addCartProduct(CartProduct.builder()
									.quantity(request.quantity())
									.totalPrice(option == null ? product.getPrice() * request.quantity() : option.getPrice() * request.quantity())
									.cart(cart)
									.product(product)
									.productOption(option)
							.build());
				}
		);
	}
	
	private Cart findCart(Long memberId) {
		return cartRepository.findByMemberId(memberId)
				.orElseGet(() -> {
					log.info("[CartService] 사용자 {}의 장바구니가 없어 새로 생성합니다.", memberId);
					return createNewCart(memberId);
				});
	}
	
	private Cart createNewCart(Long memberId) {
		User user = userRepository.findById(memberId)
				.orElseThrow(() -> new CartHandler(ErrorStatus.USER_NOT_FOUND));
		Cart newCart = Cart.builder().member(user).build();
		return cartRepository.save(newCart);
	}
}