package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.order.entity.enums.PaymentMethod;

import java.time.LocalDateTime;

public record PaymentInfoDto(
        PaymentMethod paymentMethod,
        OrderStatus paymentStatus,
        Integer paidAmount,
        LocalDateTime paidAt
) {
    public static PaymentInfoDto of(Order order) {
        return new PaymentInfoDto(
                order.getPaymentMethod(),
                order.getOrderStatus(),
                order.getPaidAmount(),
                order.getPaidAt()
        );
    }
}