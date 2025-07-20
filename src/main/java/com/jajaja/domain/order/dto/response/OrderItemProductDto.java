package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;
import lombok.Builder;

@Builder
public record OrderItemProductDto(
        long id,
        String image,
        String store,
        String name,
        String option,
        int quantity
) {
    public static OrderItemProductDto from(OrderProduct orderProduct) {
        return OrderItemProductDto.builder()
                .id(orderProduct.getProduct().getId())
                .image(orderProduct.getProduct().getImageUrl())
                .store(orderProduct.getProduct().getStore())
                .name(orderProduct.getProduct().getName())
                .option(orderProduct.getProductOption().getName())
                .quantity(orderProduct.getQuantity())
                .build();
    }
}