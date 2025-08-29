package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AllReviewListDto(
        ReviewItemDto review,
        boolean isLike,
        List<String> imageUrls,
        Long productId
) {
    public static AllReviewListDto of(
            ReviewItemDto review,
            boolean isLike,
            List<String> imageUrls,
            Long productId
    ) {
        return AllReviewListDto.builder()
                .review(review)
                .isLike(isLike)
                .imageUrls(imageUrls)
                .productId(productId)
                .build();
    }
}