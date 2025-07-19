package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findTop3ByProductIdOrderByLikeCountDesc(Long productId);
    List<Review> findAllByProductId(Long productId);
    Long countByProductId(Long productId);
    Double findAvgRatingByProductId(Long productId);
    List<String> findTop6ReviewImageUrlsByProductId(Long productId);
}
