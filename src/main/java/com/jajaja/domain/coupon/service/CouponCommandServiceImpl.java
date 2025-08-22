package com.jajaja.domain.coupon.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.cart.repository.CartRepository;
import com.jajaja.domain.coupon.dto.CouponApplyRequestDto;
import com.jajaja.domain.coupon.dto.CouponApplyResponseDto;
import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.CouponStatus;
import com.jajaja.domain.coupon.repository.CouponRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.domain.member.repository.MemberCouponRepository;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CouponHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCommandServiceImpl implements CouponCommandService{
	
	private final MemberRepository memberRepository;
	private final CouponRepository couponRepository;
	private final CartRepository cartRepository;
	private final CartProductRepository cartProductRepository;
	private final MemberCouponRepository memberCouponRepository;
	private final CouponCommonService couponCommonService;
	
	@Override
	public CouponApplyResponseDto applyCouponToCart(Long memberId, CouponApplyRequestDto request) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CouponHandler(ErrorStatus.MEMBER_NOT_FOUND));
		Coupon coupon = validateAndGetCoupon(request.couponId());
		
		MemberCoupon memberCoupon = validateCouponOwnership(member, coupon);
		validateCouponStatus(memberCoupon);
		
		Cart cart = cartRepository.findByMemberId(memberId);
		if (cart == null) {
			throw new CouponHandler(ErrorStatus.CART_NOT_FOUND);
		}
		
		List<CartProduct> selectedItems = validateAndGetSelectedItems(cart, request.items());
		couponCommonService.validateCouponForSelectedItems(selectedItems, coupon);
		cart.applyCoupon(memberCoupon);
		
		return CouponApplyResponseDto.withDiscount(cart.getId(), coupon.getId(), coupon.getName(), 
				couponCommonService.calculateDiscountForSelectedItems(selectedItems, coupon));
	}
	
	@Override
	public void unapplyCoupon(Long memberId) {
		memberRepository.findById(memberId)
				.orElseThrow(() -> new CouponHandler(ErrorStatus.MEMBER_NOT_FOUND));
		
		Cart cart = cartRepository.findByMemberId(memberId);
		if (cart == null) {
			throw new CouponHandler(ErrorStatus.CART_NOT_FOUND);
		}
		
		cart.removeCoupon();
	}
	
	/**
	 * 쿠폰 존재 여부를 검증하고 반환합니다.
	 */
	private Coupon validateAndGetCoupon(Long couponId) {
		return couponRepository.findById(couponId)
				.orElseThrow(() -> new CouponHandler(ErrorStatus.COUPON_NOT_FOUND));
	}
	
	/**
	 * 회원이 해당 쿠폰을 보유하고 있는지 검증합니다.
	 */
	private MemberCoupon validateCouponOwnership(Member member, Coupon coupon) {
		return memberCouponRepository.findByMemberAndCoupon(member, coupon)
				.orElseThrow(() -> new CouponHandler(ErrorStatus.COUPON_NOT_OWNED));
	}
	
	/**
	 * 쿠폰이 사용 가능한 상태인지 검증합니다.
	 */
	private void validateCouponStatus(MemberCoupon memberCoupon) {
		if (!memberCoupon.getStatus().equals(CouponStatus.AVAILABLE)) {
			throw new CouponHandler(ErrorStatus.COUPON_NOT_AVAILABLE);
		}
	}
	
	/**
	 * 선택된 장바구니 상품들을 검증하고 반환합니다.
	 */
	private List<CartProduct> validateAndGetSelectedItems(Cart cart, List<Long> cartProductIds) {
		List<CartProduct> selectedItems = cartProductRepository.findAllById(cartProductIds);
		
		if (selectedItems.size() != cartProductIds.size()) {
			throw new CouponHandler(ErrorStatus.CART_PRODUCT_NOT_FOUND);
		}
		
		// 선택된 상품들이 모두 해당 장바구니의 상품인지 확인
		for (CartProduct item : selectedItems) {
			if (!item.getCart().getId().equals(cart.getId())) {
				throw new CouponHandler(ErrorStatus.CART_PRODUCT_NOT_FOUND);
			}
		}
		
		return selectedItems;
	}
}
