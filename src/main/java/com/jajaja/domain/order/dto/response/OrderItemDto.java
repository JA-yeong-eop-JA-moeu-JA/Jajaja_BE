package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderItemDto(
        long orderProductId,
        OrderStatus status,
        TeamStatus teamStatus,
        LocalDateTime teamCreatedAt,
        OrderItemProductDto product,
        int price
) {
    public static OrderItemDto of(OrderProduct orderProduct, TeamStatus teamStatus, LocalDateTime teamCreatedAt) {
        return OrderItemDto.builder()
                .orderProductId(orderProduct.getId())
                .status(orderProduct.getStatus())
                .teamStatus(teamStatus)
                .teamCreatedAt(teamCreatedAt)
                .product(OrderItemProductDto.from(orderProduct))
                .price(orderProduct.getPrice())
                .build();
    }
}
