package com.jajaja.domain.review.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingReviewListResponseDto(
        PageResponse page,
        List<ReviewListDto> reviews
) {

    public static PagingReviewListResponseDto of(
            Page<ReviewItemDto> reviewPage,
            List<ReviewListDto> reviewListDtos) {
        return PagingReviewListResponseDto.builder()
                .page(PageResponse.from(reviewPage))
                .reviews(reviewListDtos)
                .build();
    }
}
