package com.jajaja.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderPrepareRequestDto {

    @NotNull(message = "구매할 상품 목록은 필수입니다.")
    private List<Long> items;

    @NotNull(message = "배송지 ID는 필수입니다.")
    private Long addressId;

    private String deliveryRequest;
    
    private Long appliedCouponId;

    @PositiveOrZero(message = "포인트는 0 이상이어야 합니다.")
    private Integer point = 0;
}