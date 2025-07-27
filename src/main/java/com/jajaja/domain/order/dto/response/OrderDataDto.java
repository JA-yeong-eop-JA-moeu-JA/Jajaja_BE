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
    private Long orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private Integer totalAmount;
    private PaymentInfoDto paymentInfo;
    private DeliveryInfoDto deliveryInfo;
    private DiscountInfoDto discountInfo;
    private LocalDateTime createdAt;

    public static OrderDataDto of(Order order) {
        return OrderDataDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getPaidAmount())
                .paymentInfo(PaymentInfoDto.of(order))
                .deliveryInfo(DeliveryInfoDto.of(order))
                .discountInfo(DiscountInfoDto.of(order))
                .createdAt(order.getCreatedAt())
                .build();
    }
}