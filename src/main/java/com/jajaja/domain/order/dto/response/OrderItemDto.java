package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import lombok.Builder;

@Builder
public record OrderItemDto(
        OrderStatus status,
        TeamStatus teamStatus,
        OrderItemProductDto product,
        int price
) {
    public static OrderItemDto of(OrderProduct orderProduct, TeamStatus teamStatus) {
        return OrderItemDto.builder()
                .status(orderProduct.getStatus())
                .teamStatus(teamStatus)
                .product(OrderItemProductDto.from(orderProduct))
                .price(orderProduct.getPrice())
                .build();
    }
}
