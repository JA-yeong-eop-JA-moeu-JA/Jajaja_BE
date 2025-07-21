package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.PagingReviewImageListResponseDto;
import com.jajaja.domain.review.dto.response.PagingReviewListResponseDto;
import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;

public interface ReviewQueryService {
    ReviewBriefResponseDto getReviewBriefInfo(Long productId);
    PagingReviewListResponseDto getReviewList(Long userId, Long productId, String sort, int page, int size);
    PagingReviewImageListResponseDto getReviewImageList(Long productId, String sort, int page, int size);
}
