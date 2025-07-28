package com.jajaja.domain.review.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingReviewableOrderListResponseDto(
        PageResponse page,
        List<ReviewableOrderListDto> orders
) {
    public static PagingReviewableOrderListResponseDto of(Page<?> page, List<ReviewableOrderListDto> orderDtos) {
        return PagingReviewableOrderListResponseDto.builder()
                .page(PageResponse.from(page))
                .orders(orderDtos)
                .build();
    }
}
