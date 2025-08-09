package com.jajaja.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPrepareResponseDto {
    private String orderId;
    private String orderName;
    private Integer totalAmount;
    private Integer discountAmount;
    private Integer pointDiscount;
    private Integer shippingFee;
    private Integer finalAmount;

    public static OrderPrepareResponseDto of(String orderId, String orderName, int totalAmount, int discountAmount,
                                           int pointDiscount, int shippingFee, int finalAmount) {
        return OrderPrepareResponseDto.builder()
                .orderId(orderId)
                .orderName(orderName)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .pointDiscount(pointDiscount)
                .shippingFee(shippingFee)
                .finalAmount(finalAmount)
                .build();
    }
}