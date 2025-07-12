package com.jajaja.domain.cart.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartProductAddRequestDto (
		@NotNull(message =  "상품 ID는 필수입니다.")
		Long productId,
		Long optionId,
		
		@NotNull	(message = "수량은 필수입니다.")
		@Min(value = 1, message = "수량은 1개 이상입니다.")
		Integer quantity
) {
}
