package com.jajaja.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderApproveRequestDto {
    
    @NotNull(message = "결제 고유번호는 필수입니다.")
    private String orderId;
    
    @NotNull(message = "payment Key는 필수입니다.")
    private String paymentKey;
    
    @NotNull(message = "결제 금액은 필수입니다.")
    private Integer paidAmount;
}