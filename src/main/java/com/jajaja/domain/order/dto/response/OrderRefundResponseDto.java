package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;

public record OrderRefundResponseDto(
        Long id,
        String orderId,
        OrderStatus orderStatus,
        Integer refundAmount,
        Integer pointRefundAmount,
        String refundReason
) {
    public static OrderRefundResponseDto of(Order order, int pointRefundAmount, String refundReason) {
        return new OrderRefundResponseDto(
                order.getId(),
                order.getOrderId(),
                order.getOrderStatus(),
                order.getPaidAmount(),
                pointRefundAmount,
                refundReason
        );
    }
}