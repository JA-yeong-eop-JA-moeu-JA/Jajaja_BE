package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.delivery.dto.DeliveryInfoDto;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderDataDto {
    private Long id;
    private String orderId;
    private OrderStatus orderStatus;
    private Integer totalAmount;
    private PaymentInfoDto paymentInfo;
    private DeliveryInfoDto deliveryInfo;
    private LocalDateTime createdAt;

    public static OrderDataDto of(Order order) {
        return OrderDataDto.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getPaidAmount())
                .paymentInfo(PaymentInfoDto.of(order))
                .deliveryInfo(DeliveryInfoDto.of(order))
                .createdAt(order.getCreatedAt())
                .build();
    }
}