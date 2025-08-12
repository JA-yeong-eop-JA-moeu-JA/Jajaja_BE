package com.jajaja.domain.point.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.review.entity.Review;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record PointHistoryDto(
        long id,
        Long orderId,
        Long reviewId,
        PointType type,
        String productName,
        int amount,
        LocalDate expiresAt,
        LocalDateTime createdAt
) {
    public static PointHistoryDto from(Point point) {
        return PointHistoryDto.builder()
                .id(point.getId())
                .orderId(Optional.ofNullable(point.getOrder())
                        .map(Order::getId)
                        .orElse(null))
                .reviewId(Optional.ofNullable(point.getReview())
                        .map(Review::getId)
                        .orElse(null))
                .type(point.getType())
                .productName(Optional.ofNullable(point.getReview())
                        .map(Review::getProduct)
                        .map(Product::getName)
                        .orElse(null))
                .amount(point.getAmount())
                .expiresAt(point.getExpiresAt())
                .createdAt(point.getCreatedAt())
                .build();
    }
}
