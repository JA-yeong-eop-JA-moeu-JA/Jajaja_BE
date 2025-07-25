package com.jajaja.domain.coupon.controller;

import com.jajaja.domain.coupon.dto.CouponApplyRequestDto;
import com.jajaja.domain.coupon.dto.CouponApplyResponseDto;
import com.jajaja.domain.coupon.dto.PagingCouponListResponseDto;
import com.jajaja.domain.coupon.service.CouponCommandService;
import com.jajaja.domain.coupon.service.CouponQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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
			description = "로그인한 사용자의 쿠폰 목록을 페이징으로 조회합니다. 최신순으로 정렬됩니다."
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
			summary = "쿠폰 적용 API",
			description = "선택한 쿠폰을 장바구니에 적용합니다."
	)
	@PostMapping("/apply")
	public ApiResponse<CouponApplyResponseDto> applyCoupon(
			@Auth Long memberId,
			@Valid @RequestBody CouponApplyRequestDto request
	) {
		return ApiResponse.onSuccess(couponCommandService.applyCouponToCart(memberId, request.couponId()));
	}
}
