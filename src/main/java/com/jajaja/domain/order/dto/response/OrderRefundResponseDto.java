package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderRefundResponseDto {
    private Long orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private Integer refundAmount;
    private Integer pointRefundAmount;
    private String refundReason;
    private LocalDateTime refundedAt;
    private LocalDateTime processedAt;

    public static OrderRefundResponseDto of(Order order, int pointRefundAmount, String refundReason) {
        return OrderRefundResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus())
                .refundAmount(order.getPaidAmount())
                .pointRefundAmount(pointRefundAmount)
                .refundReason(refundReason)
                .refundedAt(LocalDateTime.now())
                .processedAt(LocalDateTime.now())
                .build();
    }
}