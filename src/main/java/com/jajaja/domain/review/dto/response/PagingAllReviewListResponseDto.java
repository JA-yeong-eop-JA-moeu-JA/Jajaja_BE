package com.jajaja.domain.review.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingAllReviewListResponseDto(
        PageResponse page,
        List<AllReviewListDto> reviews
) {
    public static PagingAllReviewListResponseDto of(Page<?> page, List<AllReviewListDto> reviews) {
        return PagingAllReviewListResponseDto.builder()
                .page(PageResponse.from(page))
                .reviews(reviews)
                .build();
    }
}