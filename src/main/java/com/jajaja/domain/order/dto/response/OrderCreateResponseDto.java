package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    private OrderDataDto data;

    public static OrderCreateResponseDto of(Order order) {
        return OrderCreateResponseDto.builder()
                .data(OrderDataDto.of(order))
                .build();
    }
}