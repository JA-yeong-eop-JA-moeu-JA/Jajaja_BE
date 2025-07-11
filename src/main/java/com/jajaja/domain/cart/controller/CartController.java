package com.jajaja.domain.cart.controller;

import com.jajaja.domain.cart.dto.CartProductAddRequestDto;
import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.service.CartService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;
	
	@Operation(
			summary = "장바구니 조회 API",
			description = "장바구니를 조회합니다.")
	@GetMapping("/")
	public ApiResponse<CartResponseDto> getCart(@Auth Long memberId) {
		return ApiResponse.onSuccess(cartService.getCart(memberId));
	}
	
	@Operation(
			summary = "장바구니 아이템 추가 및 수정 API",
			description = "장바구니에 아이템을 추가하거나 수량을 수정합니다.")
	@PostMapping("/products")
	public ApiResponse<String> addOrUpdateCartProduct(@Auth Long memberId, @RequestBody @Valid CartProductAddRequestDto request) {
		cartService.addOrUpdateCartProduct(memberId, request);
		return ApiResponse.onSuccess("장바구니에 상품이 추가/수정되었습니다.");
	}
}
