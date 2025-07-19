package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;

public interface ReviewQueryService {
    ReviewBriefResponseDto getReviewBriefInfo(Long productId);
}
