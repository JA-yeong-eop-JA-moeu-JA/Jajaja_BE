package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewListDto(
        ReviewItemDto review,
        boolean isLike,
        List<String> imageUrls
) {
    public static ReviewListDto from(
            ReviewItemDto review,
            boolean isLike,
            List<String> imageUrls) {
        return ReviewListDto.builder()
                .review(review)
                .isLike(isLike)
                .imageUrls(imageUrls)
                .build();
    }
}