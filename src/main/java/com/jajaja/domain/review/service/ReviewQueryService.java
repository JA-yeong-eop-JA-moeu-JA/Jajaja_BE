package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.PagingReviewListResponseDto;
import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;


public interface ReviewQueryService {
    ReviewBriefResponseDto getReviewBriefInfo(Long productId);
    PagingReviewListResponseDto getReviewList(Long memberId, Long productId, String sort, int page, int size);
}
