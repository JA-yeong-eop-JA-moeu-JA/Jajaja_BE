package com.jajaja.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRefundRequestDto (

    @NotNull(message = "주문 ID는 필수입니다.")
    Long orderId,
    
    @NotNull(message = "payment Key는 필수입니다.")
    String paymentKey,
    
    @NotBlank(message = "환불 사유는 필수입니다.")
    String refundReason
) {
    public static OrderRefundRequestDto of(Long orderId, String paymentKey, String refundReason) {
        return new OrderRefundRequestDto(orderId, paymentKey, refundReason);
    }
}