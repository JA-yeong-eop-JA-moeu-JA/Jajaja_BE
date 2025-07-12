package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartProductDeleteRequestDto;
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
		
		CartOpterationContext context = prepareCartOperationContext(memberId, request.productId(), request.optionId());
		
		Optional<CartProduct> existingItem = cartProductRepository.findByCartIdAndProductId(context.cart().getId(), context.product().getId());
		
		existingItem.ifPresentOrElse(
				item -> {
					log.info("[CartCommandService] 기존 아이템 {}의 옵션과 수량을 변경합니다.", item.getId());
					item.update(context.productOption(), request.quantity());
				},
				() -> {
					log.info("[CartCommandService] 장바구니에 새로 아이템 {}를 추가합니다.", request.productId());
					CartProduct newCartProduct = CartProduct.create(context.cart(), context.product(), context.productOption(), request.quantity());
					context.cart().addCartProduct(newCartProduct);
					cartProductRepository.save( newCartProduct);
				}
		);
	}
	
	@Override
	public void deleteCartProduct(Long memberId, CartProductDeleteRequestDto request) {
		log.info("[CartCommandService] 사용자 {}의 장바구니에 아이템 {} 삭제", memberId, request.productId());
		
		CartOpterationContext context = prepareCartOperationContext(memberId, request.productId(), request.optionId());
		
		CartProduct existingItem = cartProductRepository.findByCartIdAndProductId(context.cart().getId(), context.product().getId())
				.orElseThrow(() -> new CartHandler((ErrorStatus.CART_PRODUCT_NOT_FOUND)));
		cartProductRepository.delete(existingItem);
	}
	
	/**
	*  장바구니 내 아이템 명령 실행에 필요한 도메인 객체를 불러오는 과정입니다.
	 *  option null 처리 등 중복되는 로직 떄문에 생성하였습니다.
	 *
	 * @return 	Cart, Product, ProductOption을 포함한 CartOpterationContext
	* */
	private CartOpterationContext prepareCartOperationContext(Long memberId, Long productId, Long optionId) {
		Cart cart = cartComponent.findCart(memberId);
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CartHandler(ErrorStatus.PRODUCT_NOT_FOUND));
		
		ProductOption option;
		if (optionId != null) {
			option = productOptionRepository.findById(optionId)
					.filter(opt -> opt.getProduct().getId().equals(productId))
					.orElseThrow(() -> new CartHandler(ErrorStatus.OPTION_NOT_FOUND));
		} else {
			option = null;
		}
		
		return new CartOpterationContext(cart, product, option);
	}
	
	private record CartOpterationContext(Cart cart, Product product, ProductOption productOption) {}
}