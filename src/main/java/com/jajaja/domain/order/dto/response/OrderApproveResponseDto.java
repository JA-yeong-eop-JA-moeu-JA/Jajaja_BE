package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderApproveResponseDto {
    private OrderDataDto data;

    public static OrderApproveResponseDto of(Order order) {
        return OrderApproveResponseDto.builder()
                .data(OrderDataDto.of(order))
                .build();
    }
}