package com.jajaja.domain.order.dto.response;

public record OrderPrepareResponseDto(
        String orderId,
        String orderName,
        Integer totalAmount,
        Integer discountAmount,
        Integer pointDiscount,
        Integer shippingFee,
        Integer finalAmount
) {
    public static OrderPrepareResponseDto of(String orderId, String orderName, int totalAmount, int discountAmount,
                                             int pointDiscount, int shippingFee, int finalAmount) {
        return new OrderPrepareResponseDto(
                orderId,
                orderName,
                totalAmount,
                discountAmount,
                pointDiscount,
                shippingFee,
                finalAmount
        );
    }
}