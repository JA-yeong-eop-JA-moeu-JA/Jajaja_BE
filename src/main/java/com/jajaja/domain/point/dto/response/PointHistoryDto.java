package com.jajaja.domain.point.dto.response;

import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record PointHistoryDto(
        long id,
        long orderId,
        PointType type,
        String productName,
        int amount,
        LocalDate expiresAt,
        LocalDateTime createdAt
) {
    public static PointHistoryDto from(Point point) {
        return PointHistoryDto.builder()
                .id(point.getId())
                .orderId(point.getOrderProduct().getOrder().getId())
                .type(point.getType())
                .productName(point.getOrderProduct().getProduct().getName())
                .amount(point.getAmount())
                .expiresAt(point.getExpiresAt())
                .createdAt(point.getCreatedAt())
                .build();
    }
}
