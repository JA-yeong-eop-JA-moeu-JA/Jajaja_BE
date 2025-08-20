package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.*;


public interface ReviewQueryService {
    ReviewBriefResponseDto getReviewBriefInfo(Long productId);
    PagingReviewListResponseDto getReviewList(Long memberId, Long productId, String sort, int page, int size);
    PagingReviewImageListResponseDto getReviewImageList(Long productId, String sort, int page, int size);
    PagingReviewListResponseDto getMyReviewList(Long memberId, int page, int size);
    PagingAllReviewListResponseDto getAllReviewList(Long memberId, String sort, int page, int size);
    PagingReviewableOrderListResponseDto getReviewableProducts(Long memberId, int page, int size);
}
