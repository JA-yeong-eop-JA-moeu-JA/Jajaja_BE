package com.jajaja.domain.cart.controller;

import com.jajaja.domain.cart.dto.CartResponseDto;
import com.jajaja.domain.cart.service.CartService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping("/")
	public ApiResponse<CartResponseDto> getCart(@Auth Long memberId) {
		return ApiResponse.onSuccess(cartService.getCart(memberId));
	}
}
