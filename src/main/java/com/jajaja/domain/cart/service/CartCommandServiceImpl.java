package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.converter.CartConverter;
import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartProductResponseDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;
import com.jajaja.domain.product.repository.ProductOptionRepository;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.service.ProductCommonService;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamCommandRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CartHandler;
import com.jajaja.global.common.dto.PriceInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartCommandServiceImpl implements CartCommandService {
	
	private final CartProductRepository cartProductRepository;
	private final ProductRepository productRepository;
	private final TeamCommandRepository teamRepository;
	private final ProductOptionRepository productOptionRepository;
	private final ProductCommonService productCommonService;
	private final CartCommonService cartCommonService;
	
	@Override
	public CartResponseDto addOrUpdateCartProduct(Long memberId, List<CartProductAddRequestDto> request) {
		Cart cart = cartCommonService.findCart(memberId);
		List<CartProductResponseDto> items = request.stream().map(req -> {
			log.info("[CartCommandService] 사용자 {}의 장바구니에 아이템 {} 추가/수정", memberId, req.productId());
			
			CartOperationContext context = prepareCartOperationContext(req.productId(), req.optionId());
			
			Optional<CartProduct> existingItem = req.optionId() != null ? cartProductRepository.findByCartIdAndProductIdAndProductOptionId(cart.getId(), context.product().getId(), context.productOption.getId())
					: cartProductRepository.findByCartIdAndProductIdAndProductOptionIsNull(cart.getId(), context.product().getId());
			
			CartProduct cartProduct = existingItem
					.map(item -> {
						log.info("[CartCommandService] 기존 아이템 {}의 옵션과 수량을 변경합니다.", item.getId());
						item.update(context.productOption(), req.quantity());
						return item;
					})
					.orElseGet(() -> {
						log.info("[CartCommandService] 장바구니에 새로 아이템 {}를 추가합니다.", req.productId());
						CartProduct newCartProduct = CartProduct.create(cart, context.product(), context.productOption(), req.quantity());
						cart.addCartProduct(newCartProduct);
						cartProductRepository.save(newCartProduct);
						return newCartProduct;
					});
			
			boolean isTeamAvailable = teamRepository.existsByProductIdAndStatus(
					cartProduct.getProduct().getId(), TeamStatus.MATCHING);
			return CartProductResponseDto.of(cartProduct, productCommonService.calculateDiscountedPrice(cartProduct.getUnitPrice(), cartProduct.getProduct().getDiscountRate()), isTeamAvailable);
		}).toList();
		
		PriceInfoDto priceInfo = PriceInfoDto.noDiscount(cart.calculateTotalAmount());
		
		return CartConverter.toCartResponseDto(cart, items, priceInfo);
	}
	
	@Override
	public void deleteCartProduct(Long memberId, Long productId, Long optionId) {
		log.info("[CartCommandService] 사용자 {}의 장바구니에 아이템 {} 삭제", memberId, productId);
		Cart cart = cartCommonService.findCart(memberId);
		
		try {
			cart.deleteCartProduct(productId, optionId);
		} catch (IllegalArgumentException e) {
			throw new CartHandler(ErrorStatus.CART_PRODUCT_NOT_FOUND);
		}
	}

	@Override
	public void deleteCartProducts(Long memberId, List<Long> cartProductIds) {
		log.info("[CartCommandService] 사용자 {}의 장바구니에서 선택된 아이템들 삭제: {}", memberId, cartProductIds);
		Cart cart = cartCommonService.findCart(memberId);
		
		List<CartProduct> cartProductsToDelete = cartProductRepository.findAllById(cartProductIds);
		
		// 해당 카트의 상품인지 검증
		cartProductsToDelete.forEach(cartProduct -> {
			if (!cartProduct.getCart().getId().equals(cart.getId())) {
				throw new CartHandler(ErrorStatus.CART_PRODUCT_NOT_FOUND);
			}
		});
		
		cartProductRepository.deleteAll(cartProductsToDelete);
		cart.getCartProducts().removeAll(cartProductsToDelete);
	}
	
	/**
	*  장바구니 내 아이템 명령 실행에 필요한 도메인 객체를 불러오는 과정입니다.
	 *  option null 처리 등 중복되는 로직 때문에 생성하였습니다.
	 *
	 * @return 	Cart, Product, ProductOption을 포함한 CartOperationContext
	* */
	private CartOperationContext prepareCartOperationContext(Long productId, Long optionId) {
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
		
		return new CartOperationContext(product, option);
	}
	
	private record CartOperationContext(Product product, ProductOption productOption) {}
}