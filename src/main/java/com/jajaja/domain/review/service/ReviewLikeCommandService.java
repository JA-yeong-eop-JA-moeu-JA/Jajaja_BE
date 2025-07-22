package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.ReviewLikeResponseDto;

public interface ReviewLikeCommandService {
    ReviewLikeResponseDto patchReviewLike(Long memberId, Long reviewId);
}
