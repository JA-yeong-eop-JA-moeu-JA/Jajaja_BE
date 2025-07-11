package com.jajaja.domain.review.converter;


import com.jajaja.domain.review.dto.response.ReviewResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewImage;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

    public static ReviewResponseDto toReviewResponseDto(
            Review review,
            boolean isLike
    ) {
        List<String> imageUrls = review.getReviewImages().stream()
                .map(image -> image.getImageUrl())
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
