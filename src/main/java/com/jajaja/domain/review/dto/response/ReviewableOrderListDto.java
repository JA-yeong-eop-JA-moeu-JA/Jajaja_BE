package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewableOrderListDto(
        long id,
        LocalDateTime date,
        List<ReviewableOrderItemDto> items
) {
    public static ReviewableOrderListDto of(long orderId, LocalDateTime date, List<ReviewableOrderItemDto> items) {
        return ReviewableOrderListDto.builder()
                .id(orderId)
                .date(date)
                .items(items)
                .build();
    }
}
