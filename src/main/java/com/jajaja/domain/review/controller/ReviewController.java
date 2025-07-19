package com.jajaja.domain.review.controller;

import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;
import com.jajaja.domain.review.service.ReviewQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewQueryService reviewQueryService;

    @Operation(
            summary = "리뷰 상세 조회 - 상단 API | by 지지/이지희",
            description = "리뷰 상세 조회의 상단(리뷰 개수, 평점, 이미지 미리보기(6개)를 조회하는 기능입니다."
    )
    @GetMapping("/{productId}")
    public ApiResponse<ReviewBriefResponseDto> getReviewBriefInfo (@PathVariable Long productId) {
        ReviewBriefResponseDto responseDto = reviewQueryService.getReviewBriefInfo(productId);
        return ApiResponse.onSuccess(responseDto);
    }
}
