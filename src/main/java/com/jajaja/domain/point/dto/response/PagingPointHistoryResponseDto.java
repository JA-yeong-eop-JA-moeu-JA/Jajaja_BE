package com.jajaja.domain.point.dto.response;

import com.jajaja.domain.point.entity.Point;
import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingPointHistoryResponseDto(
        PageResponse page,
        int pointBalance,
        List<PointHistoryDto> pointHistories
) {
    public static PagingPointHistoryResponseDto of(Page<Point> pointPage, int pointBalance, List<PointHistoryDto> pointHistoryDtos) {
        return PagingPointHistoryResponseDto.builder()
                .page(PageResponse.from(pointPage))
                .pointBalance(pointBalance)
                .pointHistories(pointHistoryDtos)
                .build();
    }
}
