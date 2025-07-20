package com.jajaja.domain.review.repository;

import java.util.List;
import java.util.Map;

public interface ReviewImageRepositoryCustom {
    Map<Integer, List<String>> findTop6ImageUrlsGroupedByReviewIds(List<Integer> reviewIds);
}
