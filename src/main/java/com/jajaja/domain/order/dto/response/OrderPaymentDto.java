package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.PaymentMethod;
import lombok.Builder;

@Builder
public record OrderPaymentDto(
        PaymentMethod method,
        int amount,
        int discount,
        int pointUsed,
        int shippingFee,
        int finalAmount
) {
    public static OrderPaymentDto from(Order order) {
        return OrderPaymentDto.builder()
                .method(order.getPaymentMethod())
                .amount(order.calculateAmount())
                .discount(order.getDiscountAmount())
                .pointUsed(order.getPointUsedAmount())
                .shippingFee(order.getShippingFee())
                .finalAmount(order.calculateFinalAmount())
                .build();
    }
}
