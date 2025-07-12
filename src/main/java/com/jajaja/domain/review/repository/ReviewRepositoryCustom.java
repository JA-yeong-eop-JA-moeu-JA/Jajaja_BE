package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findTop3ByProductIdOrderByLikeCountDesc(Long productId);
}
