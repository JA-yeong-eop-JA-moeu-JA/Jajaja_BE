package com.jajaja.domain.order.dto.request;

import com.jajaja.domain.order.entity.enums.OrderType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record OrderPrepareRequestDto (

    @NotNull(message = "구매할 상품 목록은 필수입니다.")
    List<Long> items,
    
    @NotNull(message = "배송지 ID는 필수입니다.")
    Long addressId,
    
    @NotNull(message = "결제 타입은 필수입니다.")
    OrderType orderType,
    
    Long teamId,
    
    String deliveryRequest,
    
    Long appliedCouponId,

    @PositiveOrZero(message = "포인트는 0 이상이어야 합니다.")
    Integer point
) {}