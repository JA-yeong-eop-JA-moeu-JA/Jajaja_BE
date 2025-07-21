package com.jajaja.domain.review.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingReviewImageListResponseDto(
        PageResponse page,
        List<ReviewImageListDto> images
) {
    public static PagingReviewImageListResponseDto of(
            Page<ReviewImageListDto> reviePhotopPage,
            List<ReviewImageListDto> images) {
        return PagingReviewImageListResponseDto.builder()
                .page(PageResponse.from(reviePhotopPage))
                .images(images)
                .build();
    }
}
