package com.jajaja.domain.cart.controller;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.service.CartCommandService;
import com.jajaja.domain.cart.service.CartQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	
	private final CartQueryService cartQueryService;
	private final CartCommandService cartCommandService;
	
	@Operation(
			summary = "장바구니 조회 API | by 엠마/신윤지",
			description = "장바구니를 조회합니다.")
	@GetMapping("/")
	public ApiResponse<CartResponseDto> getCart(@Auth Long memberId) {
		return ApiResponse.onSuccess(cartQueryService.getCart(memberId));
	}
	
	@Operation(
			summary = "장바구니 아이템 추가 및 수정 API | by 엠마/신윤지",
			description = "장바구니에 아이템을 추가하거나 수량을 수정합니다.")
	@PostMapping("/products")
	public ApiResponse<String> addOrUpdateCartProduct(@Auth Long memberId, @RequestBody @Valid List<CartProductAddRequestDto> request) {
		cartCommandService.addOrUpdateCartProduct(memberId, request);
		return ApiResponse.onSuccess("성공적으로 장바구니에 상품이 추가/수정되었습니다.");
	}
	
	@Operation(
			summary = "장바구니 아이템 삭제 API | by 엠마/신윤지",
			description = "장바구니에 아이템을 추가하거나 수량을 수정합니다.")
	@DeleteMapping("/products")
	public ApiResponse<String> deleteCartProduct(@Auth Long memberId,
												 @RequestParam("productId") Long productId,
												 @RequestParam(name = "optionId", required = false) Long optionId) {
		cartCommandService.deleteCartProduct(memberId, productId, optionId);
		return ApiResponse.onSuccess("성공적으로  장바구니 상품이 삭제되었습니다.");
	}
}
