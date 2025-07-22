package com.jajaja.domain.review.dto.response;

import com.jajaja.domain.review.entity.Review;
import lombok.Builder;

@Builder
public record ReviewLikeResponseDto(
        int reviewId,
        Boolean isLike
) {
    public static ReviewLikeResponseDto of(Review review, boolean isLike) {
        return ReviewLikeResponseDto.builder()
                .reviewId(review.getId().intValue())
                .isLike(isLike)
                .build();
    }
}
