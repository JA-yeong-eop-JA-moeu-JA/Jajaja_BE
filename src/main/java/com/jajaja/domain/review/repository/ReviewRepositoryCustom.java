package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findTop3ByProductIdOrderByLikeCountDesc(Long productId);
    Long countByProductId(Long productId);
    Double findAvgRatingByProductId(Long productId);
    List<String> findTop6ReviewImageUrlsByProductId(Long productId);
    Page<Review> findPageByProductIdOrderByCreatedAt(Long productId, int page, int size);
    Page<Review> findPageByProductIdOrderByLikeCount(Long productId, int page, int size);
}
