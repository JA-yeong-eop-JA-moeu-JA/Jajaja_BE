package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReviewResponseDto(
        int id,
        String nickname,
        LocalDate createDate,
        double rating,
        String option,
        String content,
        boolean isLike,
        int likeCount,
        int imagesCount,
        List<String> imageUrls
) {
    public static ReviewResponseDto of(
            int id,
            String nickname,
            LocalDate createDate,
            double rating,
            String option,
            String content,
            boolean isLike,
            int likeCount,
            int imagesCount,
            List<String> imageUrls
    ) {
        return ReviewResponseDto.builder()
                .id(id)
                .nickname(nickname)
                .createDate(createDate)
                .rating(rating)
                .option(option)
                .content(content)
                .isLike(isLike)
                .likeCount(likeCount)
                .imagesCount(imagesCount)
                .imageUrls(imageUrls)
                .build();
    }
}