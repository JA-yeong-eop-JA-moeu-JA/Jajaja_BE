package com.jajaja.domain.cart.dto;

import jakarta.validation.constraints.NotNull;

public record CartProductDeleteRequestDto (
		@NotNull(message =  "상품 ID는 필수입니다.")
		Long productId,
		Long optionId
) {
}
