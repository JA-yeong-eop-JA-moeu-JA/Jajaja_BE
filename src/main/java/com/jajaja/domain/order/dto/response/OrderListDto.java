package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderListDto(
        long id,
        LocalDateTime date,
        List<OrderItemDto> items
) {
    public static OrderListDto of(Order order, List<OrderItemDto> items) {
        return OrderListDto.builder()
                .id(order.getId())
                .date(order.getCreatedAt())
                .items(items)
                .build();
    }
}
