package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.delivery.dto.DeliveryInfoDto;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderDataDto(
        Long id,
        String orderId,
        OrderStatus orderStatus,
        Integer totalAmount,
        PaymentInfoDto paymentInfo,
        DeliveryInfoDto deliveryInfo,
        LocalDateTime createdAt
) {
    public static OrderDataDto of(Order order) {
        return new OrderDataDto(
                order.getId(),
                order.getOrderId(),
                order.getOrderStatus(),
                order.getPaidAmount(),
                PaymentInfoDto.of(order),
                DeliveryInfoDto.of(order),
                order.getCreatedAt()
        );
    }
}