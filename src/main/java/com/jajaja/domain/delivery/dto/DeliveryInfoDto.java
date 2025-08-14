package com.jajaja.domain.delivery.dto;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DeliveryInfoDto (
    Long addressId,
    String deliveryRequest,
    String estimatedDeliveryDate
) {
    public static DeliveryInfoDto of(Order order) {
        return DeliveryInfoDto.builder()
                .addressId(order.getDelivery().getId())
                .deliveryRequest(order.getDeliveryRequest())
                .estimatedDeliveryDate(LocalDate.now().plusDays(2).toString())
                .build();
    }
}