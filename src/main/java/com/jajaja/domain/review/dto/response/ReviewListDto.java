package com.jajaja.domain.review.dto.response;

import com.jajaja.domain.review.entity.Review;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReviewListDto(
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
    public static ReviewListDto from(
            Review review,
            boolean isLike,
            List<String> imageUrls) {
        return ReviewListDto.builder()
                .id(review.getId().intValue())
                .nickname(review.getMember().getName())
                .createDate(review.getCreatedAt().toLocalDate())
                .rating(review.getRating())
                .option(review.getProductOption().getName())
                .content(review.getContent())
                .isLike(isLike)
                .likeCount(review.getReviewLikes().size())
                .imagesCount(review.getReviewImages().size())
                .imageUrls(imageUrls)
                .build();
    }
}