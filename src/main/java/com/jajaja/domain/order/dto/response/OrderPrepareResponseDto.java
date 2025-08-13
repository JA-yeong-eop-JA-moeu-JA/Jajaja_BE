package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.enums.OrderType;

public record OrderPrepareResponseDto(
        String orderId,
        String orderName,
        OrderType orderType,
        int totalAmount,
        int discountAmount,
        int pointDiscount,
        int shippingFee,
        int finalAmount
) {
    public static OrderPrepareResponseDto of(String orderId, String orderName, OrderType orderType, int totalAmount, int discountAmount,
                                             int pointDiscount, int shippingFee, int finalAmount) {
        return new OrderPrepareResponseDto(
                orderId,
                orderName,
                orderType,
                totalAmount,
                discountAmount,
                pointDiscount,
                shippingFee,
                finalAmount
        );
    }
}