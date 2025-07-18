package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.delivery.entity.Delivery;
import lombok.Builder;

@Builder
public record OrderDeliveryDto(
        String name,
        String phone,
        String address
) {
    public static OrderDeliveryDto from(Delivery delivery) {
        return OrderDeliveryDto.builder()
                .name(delivery.getName())
                .phone(delivery.getPhone())
                .address(delivery.getAddress())
                .build();
    }
}
