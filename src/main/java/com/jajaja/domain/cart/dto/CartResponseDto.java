package com.jajaja.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
import com.jajaja.domain.coupon.entity.enums.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 장바구니 조회 API 응답 DTO
 *
 * @param data          장바구니에 담긴 상품 목록
 * @param appliedCoupon 적용된 쿠폰 정보 (없을 경우 null)
 * @param summary       주문 금액 관련 정보
 * @param totalCount    장바구니에 담긴 총 상품 종류 수
 * @param createdAt     장바구니 생성 시각
 * @param updatedAt     장바구니 마지막 수정 시각
 */
public record CartResponseDto(
		List<CartItemInfoDto> data,
		AppliedCouponInfoDto appliedCoupon,
		SummaryInfoDto summary,
		int totalCount,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
		LocalDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
		LocalDateTime updatedAt
) {
	/**
	 * 장바구니 내 개별 상품 정보
	 *
	 * @param id               CartProduct ID
	 * @param productId        Product ID
	 * @param productName      상품명
	 * @param brand            브랜드명
	 * @param optionId          Option ID
	 * @param option           옵션명
	 * @param quantity         수량
	 * @param productThumbnail 상품 썸네일 이미지 URL
	 * @param unitPrice        상품 개당 가격
	 * @param totalPrice       할인이 적용된 최종 라인 아이템 가격 (수량 * 단가 - 할인액)
	 * @param teamAvailable    팀 구매 가능 여부
	 */
	public record CartItemInfoDto(
			Long id,
			Long productId,
			String productName,
			String brand,
			Long optionId,
			String option,
			int quantity,
			String productThumbnail,
			int unitPrice,
			int totalPrice,
			boolean teamAvailable
	) {}
	
	/**
	 * 적용된 쿠폰의 상세 정보
	 *
	 * @param couponId             Coupon ID
	 * @param couponName           쿠폰명
	 * @param discountType         할인 타입
	 * @param discountValue        할인 값
	 * @param applicableConditions 쿠폰 적용 조건
	 */
	public record AppliedCouponInfoDto(
			Long couponId,
			String couponName,
			DiscountType discountType,
			Integer discountValue,
			CouponConditionsInfoDto applicableConditions
	) {}
	
	/**
	 * 쿠폰 적용 조건 정보
	 *
	 * @param type           조건 타입
	 * @param value         조건 값
	 * @param minOrderAmount 최소 주문 금액
	 */
	public record CouponConditionsInfoDto(
			ConditionType type,
			String value,
			Integer minOrderAmount
	) {}
	
	/**
	 * 주문 금액 관련 정보
	 *
	 * @param shippingFee 배송비
	 */
	public record SummaryInfoDto(
			int shippingFee
	) {}
}