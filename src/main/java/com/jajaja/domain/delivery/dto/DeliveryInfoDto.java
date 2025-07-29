package com.jajaja.domain.delivery.dto;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DeliveryInfoDto {
    private Long addressId;
    private String deliveryRequest;
    private String estimatedDeliveryDate;

    public static DeliveryInfoDto of(Order order) {
        return DeliveryInfoDto.builder()
                .addressId(order.getDelivery().getId())
                .deliveryRequest(order.getDeliveryRequest())
                .estimatedDeliveryDate(LocalDate.now().plusDays(2).toString())
                .build();
    }
}