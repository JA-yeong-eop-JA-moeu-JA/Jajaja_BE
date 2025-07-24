package com.jajaja.domain.review.controller;

import com.jajaja.domain.review.dto.request.ReviewCreateRequestDto;
import com.jajaja.domain.review.dto.response.*;
import com.jajaja.domain.review.service.ReviewCommandService;
import com.jajaja.domain.review.service.ReviewLikeCommandService;
import com.jajaja.domain.review.service.ReviewQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewQueryService reviewQueryService;
    private final ReviewLikeCommandService reviewLikeCommandService;
    private final ReviewCommandService reviewCommandService;

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
    public ApiResponse<PagingReviewListResponseDto> getReviewList(
            @Auth Long memberId,
            @PathVariable Long productId,
            @Parameter(description = "정렬 기준 (LATEST | RECOMMEND)", example = "LATEST")
            @RequestParam(required = false, defaultValue = "NEW") String sort,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size
            ) {
        PagingReviewListResponseDto responseDto = reviewQueryService.getReviewList(memberId, productId, sort, page, size);
        return ApiResponse.onSuccess(responseDto);
    }


    @Operation(
            summary = "사진 리뷰 상세 조회 API | by 지지/이지희",
            description = "필터링(최신순|추천순 필터링)을 통해 사진 리뷰 상세 조회하는 기능입니다."
    )
    @GetMapping("/photo/{productId}")
    public ApiResponse<PagingReviewImageListResponseDto> getReviewImageList(
            @PathVariable Long productId,
            @Parameter(description = "정렬 기준 (LATEST | RECOMMEND)", example = "LATEST")
            @RequestParam(required = false, defaultValue = "NEW") String sort,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "15")
            @RequestParam(defaultValue = "15") int size
    ) {
        PagingReviewImageListResponseDto responseDto = reviewQueryService.getReviewImageList(productId, sort, page, size);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(
            summary = "리뷰 좋아요/취소 API | by 지지/이지희",
            description = "'하트' 버튼을 통해 해당 리뷰의 좋아요/취소를 수행하는 기능입니다."
    )
    @PatchMapping("/{reviewId}")
    public ApiResponse<ReviewLikeResponseDto> patchReviewLike(
            @Auth Long memberId,
            @PathVariable Long reviewId
    ) {
        ReviewLikeResponseDto responseDto = reviewLikeCommandService.patchReviewLike(memberId, reviewId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(
            summary = "리뷰 추가 API | by 루비/이송미",
            description = """
                          구매한 상품에 대해 리뷰를 추가합니다.

                          - 별점, 내용, 사진(최대 6장)을 포함할 수 있습니다.
                          - 이미지는 S3 Presigned URL을 통해 업로드한 후,
                            해당 key 값을 imageKeys로 전달해주세요.
                          """
    )
    @PostMapping("/{productId}")
    public ApiResponse<ReviewCreateResponseDto> createReview(
            @Auth Long memberId,
            @PathVariable Long productId,
            @Valid @RequestBody ReviewCreateRequestDto requestDto
    ) {
        Long reviewId = reviewCommandService.createReview(memberId, productId, requestDto);
        return ApiResponse.onSuccess(new ReviewCreateResponseDto(reviewId));
    }

}
