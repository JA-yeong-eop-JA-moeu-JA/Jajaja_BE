package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentInfoDto {
    private PaymentMethod paymentMethod;
    private String paymentStatus;
    private Integer paidAmount;
    private LocalDateTime paidAt;

    public static PaymentInfoDto of(Order order) {
        return PaymentInfoDto.builder()
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus("COMPLETED")
                .paidAmount(order.getPaidAmount())
                .paidAt(order.getPaidAt())
                .build();
    }
}