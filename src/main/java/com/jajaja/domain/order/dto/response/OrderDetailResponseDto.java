package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDetailResponseDto(
        LocalDateTime date,
        String orderNumber,
        List<OrderItemDto> items,
        OrderDeliveryDto delivery,
        OrderPaymentDto payment
) {
    public static OrderDetailResponseDto of(Order order, List<OrderItemDto> items) {
        return OrderDetailResponseDto.builder()
                .date(order.getCreatedAt())
                .orderNumber(order.getOrderNumber())
                .items(items)
                .delivery(OrderDeliveryDto.from(order.getDelivery()))
                .payment(OrderPaymentDto.from(order))
                .build();
    }
}
