package com.jajaja.domain.cart.dto;

import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;
import lombok.Builder;

@Builder
public record CartProductResponseDto(
		Long id,
		Long productId,
		String productName,
		String brand,
		Long optionId,
		String optionName,
		int quantity,
		String productThumbnail,
		int unitPrice,
		int totalPrice,
		boolean teamAvailable
) {
	public static CartProductResponseDto of(CartProduct cartProduct, boolean isTeamAvailable) {
		Product product = cartProduct.getProduct();
		ProductOption option = cartProduct.getProductOption();
		
		Long optionId = (option != null) ? option.getId() : null;
		String optionName = (option != null) ? option.getName() : "";
		Integer price = (option != null) ? option.getPrice() : product.getPrice();
		
		return CartProductResponseDto.builder()
				.id(cartProduct.getId())
				.productId(product.getId())
				.brand(product.getStore())
				.productName(product.getName())
				.optionId(optionId)
				.optionName(optionName)
				.quantity(cartProduct.getQuantity())
				.productThumbnail(product.getThumbnailUrl())
				.unitPrice(price)
				.totalPrice(cartProduct.getUnitPrice() * cartProduct.getQuantity())
				.teamAvailable(isTeamAvailable)
				.build();
	}
}