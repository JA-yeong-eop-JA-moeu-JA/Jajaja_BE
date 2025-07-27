package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.request.ReviewCreateRequestDto;

public interface ReviewCommandService {
    Long createReview(Long memberId, Long productId, ReviewCreateRequestDto requestDto);
    void deleteReview(Long memberId, Long reviewId);
}
