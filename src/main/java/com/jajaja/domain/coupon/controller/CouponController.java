package com.jajaja.domain.coupon.controller;

import com.jajaja.domain.coupon.dto.CouponApplyResponseDto;
import com.jajaja.domain.coupon.dto.PagingCouponListResponseDto;
import com.jajaja.domain.coupon.service.CouponCommandService;
import com.jajaja.domain.coupon.service.CouponQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
	
	private final CouponQueryService couponQueryService;
	private final CouponCommandService couponCommandService;
	
	@Operation(
			summary = "내 쿠폰 목록 조회 AP",
			description = "로그인한 사용자가 사용할 수 있는 쿠폰 목록을 페이징으로 조회합니다. 최신순으로 정렬됩니다."
	)
	@GetMapping
	public ApiResponse<PagingCouponListResponseDto> getMyCoupons(
			@Auth Long memberId,
			@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
			@RequestParam(defaultValue = "0") int page,
			@Parameter(description = "페이지 크기", example = "4")
			@RequestParam(defaultValue = "4") int size
	) {
		return ApiResponse.onSuccess(couponQueryService.getCouponsByMemberIdWithPaging(memberId, page, size));
	}
	
	@Operation(
			summary = "쿠폰 적용/수정 API",
			description = "선택한 쿠폰을 장바구니에 적용합니다."
	)
	@PostMapping("/{couponId}/apply")
	public ApiResponse<CouponApplyResponseDto> applyCoupon(
			@Auth Long memberId,
			@PathVariable Long couponId
	) {
		return ApiResponse.onSuccess(couponCommandService.applyCouponToCart(memberId, couponId));
	}
	
	@Operation(
			summary = "쿠폰 취소 API",
			description = "선택한 쿠폰을 장바구니에서 취소합니다."
	)
	@DeleteMapping("/unapply")
	public ApiResponse<String> unapplyCoupon(@Auth Long memberId) {
		couponCommandService.unapplyCoupon(memberId);
		return ApiResponse.onSuccess("성공적으로 쿠폰 적용을 취소하였습니다.");
	}
}
