package com.jajaja.domain.review.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewableOrderItemDto(
        Long orderId,
        LocalDateTime orderDate,
        Long orderProductId,
        Long productId,
        String productName,
        String imageUrl,
        int price,
        int quantity,
        boolean isReviewWritten
) {
    public static ReviewableOrderItemDto of(OrderProduct op, boolean isWritten) {
        return ReviewableOrderItemDto.builder()
                .orderId(op.getOrder().getId())
                .orderDate(op.getOrder().getCreatedAt())
                .orderProductId(op.getId())
                .productId(op.getProduct().getId())
                .productName(op.getProduct().getName())
                .imageUrl(op.getProduct().getThumbnailUrl())
                .price(op.getPrice())
                .quantity(op.getQuantity())
                .isReviewWritten(isWritten)
                .build();
    }
}