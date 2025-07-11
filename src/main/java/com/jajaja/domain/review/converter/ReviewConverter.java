package com.jajaja.domain.review.converter;


import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewImage;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

    public static ReviewResponseDto toReviewResponseDto(Review review, Long userId) {
        // 비회원일 경우
        boolean isLike = false;

        // 회원일 경우
        if (userId != null) {
            isLike = review.getReviewLikes().stream()
                    .anyMatch(like -> like.getMember().getId().equals(userId));
        }

        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        return ReviewResponseDto.of(
                review.getId().intValue(),
                review.getMember().getName(),
                review.getCreatedAt().toLocalDate(),
                review.getRating(),
                review.getProductOption().getName(),
                review.getContent(),
                isLike,
                review.getReviewLikes().size(),
                review.getReviewImages().size(),
                imageUrls
        );
    }
}
