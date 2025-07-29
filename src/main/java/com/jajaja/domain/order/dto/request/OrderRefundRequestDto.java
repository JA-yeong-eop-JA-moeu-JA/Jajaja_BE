package com.jajaja.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRefundRequestDto {

    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId;

    @NotBlank(message = "환불 사유는 필수입니다.")
    private String refundReason;

    public static OrderRefundRequestDto of(Long orderId, String refundReason) {
        return new OrderRefundRequestDto(orderId, refundReason);
    }
}