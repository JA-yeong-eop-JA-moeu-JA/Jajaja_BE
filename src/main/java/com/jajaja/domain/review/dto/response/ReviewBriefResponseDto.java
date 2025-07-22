package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewBriefResponseDto(
        Integer reviewCount,
        double avgRating,
        Integer imagesCount,
        List<String> imageUrls) {

    public static ReviewBriefResponseDto of(
            Integer reviewCount,
            double avgRating,
            List<String> imageUrls
    ) {
        return ReviewBriefResponseDto.builder()
                .reviewCount(reviewCount)
                .avgRating(avgRating)
                .imagesCount(imageUrls.size())
                .imageUrls(imageUrls)
                .build();
    }

}
