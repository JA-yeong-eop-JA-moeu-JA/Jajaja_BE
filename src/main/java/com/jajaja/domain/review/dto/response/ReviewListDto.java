package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewListDto(
        ReviewItemDto reviewPage,
        boolean isLike,
        List<String> imageUrls
) {
    public static ReviewListDto from(
            ReviewItemDto reviewPage,
            boolean isLike,
            List<String> imageUrls) {
        return ReviewListDto.builder()
                .reviewPage(reviewPage)
                .isLike(isLike)
                .imageUrls(imageUrls)
                .build();
    }
}