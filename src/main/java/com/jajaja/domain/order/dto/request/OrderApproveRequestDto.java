package com.jajaja.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderApproveRequestDto (
    @NotNull(message = "결제 고유번호는 필수입니다.")
    String orderId,
    
    @NotNull(message = "payment Key는 필수입니다.")
    String paymentKey,
    
    @NotNull(message = "결제 금액은 필수입니다.")
    Integer paidAmount
) {}