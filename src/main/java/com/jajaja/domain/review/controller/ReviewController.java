package com.jajaja.domain.review.controller;

import com.jajaja.domain.review.dto.response.PagingReviewListResponseDto;
import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;
import com.jajaja.domain.review.service.ReviewQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewQueryService reviewQueryService;

    @Operation(
            summary = "리뷰 상세 조회 - 상단 API | by 지지/이지희",
            description = "리뷰 상세 조회의 상단(리뷰 개수, 평점, 이미지 미리보기(6개)을 조회하는 기능입니다."
    )
    @GetMapping("/info/{productId}")
    public ApiResponse<ReviewBriefResponseDto> getReviewBriefInfo (@PathVariable Long productId) {
        ReviewBriefResponseDto responseDto = reviewQueryService.getReviewBriefInfo(productId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(
            summary = "리뷰 상세 조회 - 하단 API | by 지지/이지희",
            description = "리뷰 상세 조회의 하단(최신순|추천순 필터링)을 조회하는 기능입니다."
    )
    @GetMapping("/{productId}")
    public ApiResponse<PagingReviewListResponseDto> getReviewList(@Auth Long userId, @RequestParam String sortType, @PathVariable Long productId, Pageable pageable) {
        PagingReviewListResponseDto responseDto = reviewQueryService.getReviewList(userId, productId, sortType, pageable);
        return ApiResponse.onSuccess(responseDto);
    }
}
