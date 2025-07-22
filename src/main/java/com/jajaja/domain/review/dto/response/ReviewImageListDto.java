package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewImageListDto(
        int photoId,
        int reviewId,
        String imageUrl,
        LocalDateTime createdAt
) {
}
