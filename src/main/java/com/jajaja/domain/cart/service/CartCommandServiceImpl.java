package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;
import com.jajaja.domain.product.repository.ProductOptionRepository;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CartHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartCommandServiceImpl implements CartCommandService {
	
	private final CartComponent cartComponent;
	private final CartProductRepository cartProductRepository;
	private final ProductRepository productRepository;
	private final ProductOptionRepository productOptionRepository;
	
	@Override
	public void addOrUpdateCartProduct(Long memberId, CartProductAddRequestDto request) {
		log.info("[CartCommandService] 사용자 {}의 장바구니에 아이템 {} 추가/수정", memberId, request.productId());
		
		Cart cart = cartComponent.findCart(memberId);
		
		Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new CartHandler(ErrorStatus.PRODUCT_NOT_FOUND));
		
		ProductOption option;
		if (request.optionId() != null) {
			option = productOptionRepository.findById(request.optionId())
					.filter(opt -> opt.getProduct().getId().equals(product.getId()))
					.orElseThrow(() -> new CartHandler(ErrorStatus.OPTION_NOT_FOUND));
		} else {
			option = null;
		}
		
		Optional<CartProduct> existingItem = cartProductRepository.findByCartIdAndProductId(cart.getId(), product.getId());
		
		existingItem.ifPresentOrElse(
				item -> {
					log.info("[CartCommandService] 기존 아이템 {}의 옵션과 수량을 변경합니다.", item.getId());
					item.update(option, request.quantity());
				},
				() -> {
					log.info("[CartCommandService] 장바구니에 새로 아이템 {}를 추가합니다.", request.productId());
					CartProduct newCartProduct = CartProduct.create(cart, product, option, request.quantity());
					cart.addCartProduct(newCartProduct);
					cartProductRepository.save( newCartProduct);
				}
		);
	}
}