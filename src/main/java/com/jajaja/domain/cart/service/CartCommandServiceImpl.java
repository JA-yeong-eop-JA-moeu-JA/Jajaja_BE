package com.jajaja.domain.cart.service;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.coupon.service.CouponCommonService;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.domain.member.repository.MemberCouponRepository;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartCommandServiceImpl implements CartCommandService {
	
	private final CartCommonService cartCommonService;
	private final CouponCommonService couponCommonService;
	private final CartProductRepository cartProductRepository;
	private final ProductRepository productRepository;
	private final ProductOptionRepository productOptionRepository;
	private final MemberCouponRepository memberCouponRepository;
	
	@Override
	public void addOrUpdateCartProduct(Long memberId, List<CartProductAddRequestDto> request) {
		Cart cart = cartCommonService.findCart(memberId);
		request.forEach(req -> {
			log.info("[CartCommandService] 사용자 {}의 장바구니에 아이템 {} 추가/수정", memberId, req.productId());
			
			CartOpterationContext context = prepareCartOperationContext(req.productId(), req.optionId());
			
			Optional<CartProduct> existingItem = req.optionId() != null ? cartProductRepository.findByCartIdAndProductIdAndProductOptionId(cart.getId(), context.product().getId(), context.productOption.getId())
					: cartProductRepository.findByCartIdAndProductIdAndProductOptionIsNull(cart.getId(), context.product().getId());
			
			existingItem.ifPresentOrElse(
					item -> {
						log.info("[CartCommandService] 기존 아이템 {}의 옵션과 수량을 변경합니다.", item.getId());
						item.update(context.productOption(), req.quantity());
					},
					() -> {
						log.info("[CartCommandService] 장바구니에 새로 아이템 {}를 추가합니다.", req.productId());
						CartProduct newCartProduct = CartProduct.create(cart, context.product(), context.productOption(), req.quantity());
						cart.addCartProduct(newCartProduct);
						cartProductRepository.save( newCartProduct);
					}
			);
		});
		revalidateAppliedCouponIfExists(cart);
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
		revalidateAppliedCouponIfExists(cart);
	}
	
	/**
	*  장바구니 내 아이템 명령 실행에 필요한 도메인 객체를 불러오는 과정입니다.
	 *  option null 처리 등 중복되는 로직 때문에 생성하였습니다.
	 *
	 * @return 	Cart, Product, ProductOption을 포함한 CartOpterationContext
	* */
	private CartOpterationContext prepareCartOperationContext(Long productId, Long optionId) {
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
		
		return new CartOpterationContext(product, option);
	}
	
	private record CartOpterationContext(Product product, ProductOption productOption) {}
	
	/**
	 * 장바구니에 쿠폰이 적용되어 있는 경우 쿠폰 적용 조건을 재검증합니다.
	 * 조건에 맞지 않으면 쿠폰을 자동으로 해제합니다.
	 */
	private void revalidateAppliedCouponIfExists(Cart cart) {
		if (cart.getCoupon() == null) {
			return;
		}
		
		try {
			MemberCoupon memberCoupon = memberCouponRepository.findByMemberAndCoupon(cart.getMember(), cart.getCoupon())
					.orElse(null);
			
			if (memberCoupon == null) {
				log.warn("[CartCommandService] 적용된 쿠폰의 MemberCoupon 정보를 찾을 수 없어 쿠폰을 해제합니다. cartId: {}, couponId: {}", 
						cart.getId(), cart.getCoupon().getId());
				cart.removeCoupon();
				return;
			}
			
			couponCommonService.validateCouponEligibility(cart, cart.getCoupon());
			log.info("[CartCommandService] 적용된 쿠폰이 여전히 유효합니다. cartId: {}, couponId: {}", 
					cart.getId(), cart.getCoupon().getId());
			
		} catch (Exception e) {
			log.warn("[CartCommandService] 장바구니 변경으로 인해 쿠폰 조건이 맞지 않아 쿠폰을 해제합니다. cartId: {}, couponId: {}, reason: {}", 
					cart.getId(), cart.getCoupon().getId(), e.getMessage());
			cart.removeCoupon();
		}
	}
}