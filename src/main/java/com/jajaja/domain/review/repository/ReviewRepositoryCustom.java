package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.domain.review.dto.response.ReviewableOrderItemDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewItemDto> findTop3ItemByProductIdOrderByLikeCountDesc(Long productId);
    Long countByProductId(Long productId);
    Double findAvgRatingByProductId(Long productId);
    List<String> findTop6ReviewImageUrlsByProductId(Long productId);
    Page<ReviewItemDto> findPageByProductIdOrderByCreatedAt(Long productId, int page, int size);
    Page<ReviewItemDto> findPageByProductIdOrderByLikeCount(Long productId, int page, int size);
    Page<ReviewItemDto> findPageByMemberIdOrderByCreatedAt(Long memberId, int page, int size);
    Page<ReviewItemDto> findPageAllOrderByCreatedAt(int page, int size);
    Page<ReviewItemDto> findPageAllOrderByLikeCount(int page, int size);
    Long findProductIdByReviewId(int reviewId);
    Page<ReviewableOrderItemDto> findPageReviewableByMemberId(Long memberId, int page, int size);
}
