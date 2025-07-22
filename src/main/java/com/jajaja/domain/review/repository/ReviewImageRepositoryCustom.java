package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.dto.response.ReviewImageListDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReviewImageRepositoryCustom {
    Map<Integer, List<String>> findTop6ImageUrlsGroupedByReviewIds(List<Integer> reviewIds);
    Page<ReviewImageListDto> findPagedPhotoListByProductIdOrderByLikeCount(Long productId, Integer page, Integer size);
    Page<ReviewImageListDto> findPagedPhotoListByProductIdOrderByCreatedAt(Long productId, Integer page, Integer size);
}
