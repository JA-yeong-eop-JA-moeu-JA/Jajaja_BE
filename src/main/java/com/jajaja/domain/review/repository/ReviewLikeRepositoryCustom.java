package com.jajaja.domain.review.repository;

import java.util.List;
import java.util.Set;

public interface ReviewLikeRepositoryCustom {
    Set<Integer> findReviewIdsLikedByUser(Long userId, List<Integer> reviewIds);
}
