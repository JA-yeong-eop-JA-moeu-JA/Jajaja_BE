package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.ReviewItemDto;
import java.util.List;

public interface ReviewCommonService {
    List<ReviewItemDto> changeReviewWriterProfile(List<ReviewItemDto> reviewItemDtoList);
}