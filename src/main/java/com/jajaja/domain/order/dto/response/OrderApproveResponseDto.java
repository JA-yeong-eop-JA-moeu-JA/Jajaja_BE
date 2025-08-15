package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;

public record OrderApproveResponseDto(
        OrderDataDto data
) {
    public static OrderApproveResponseDto of(Order order) {
        return new OrderApproveResponseDto(OrderDataDto.of(order));
    }
    
    public record OrderDataDto(
            Long orderId,
            String orderName,
            int finalAmount
    ) {
        public static OrderDataDto of(Order order) {
            return new OrderDataDto(
                    order.getId(),
                    order.getOrderName(),
                    order.getPaidAmount()
            );
        }
    }
}