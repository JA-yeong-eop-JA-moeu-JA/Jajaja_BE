package com.jajaja.domain.review.converter;


import com.jajaja.domain.review.dto.response.ReviewListDto;
import com.jajaja.domain.review.entity.Review;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

    public static ReviewListDto toReviewResponseDto(
            Review review,
            boolean isLike
    ) {
        List<String> imageUrls = review.getReviewImages().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());

        return ReviewListDto.from(
                review,
                isLike,
                imageUrls
        );
    }
}
