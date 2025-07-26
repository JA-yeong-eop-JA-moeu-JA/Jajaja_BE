package com.jajaja.domain.point.controller;

import com.jajaja.domain.point.dto.response.PagingPointHistoryResponseDto;
import com.jajaja.domain.point.dto.response.PointBalanceResponseDto;
import com.jajaja.domain.point.service.PointCommandService;
import com.jajaja.domain.point.service.PointQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
@Tag(name = "Point", description = "포인트 API")
public class PointController {

    private final PointQueryService pointQueryService;
    private final PointCommandService pointCommandService;

    @Operation(
            summary = "포인트 잔액 조회 | by 지안/윤진수",
            description = "로그인한 사용자의 포인트 잔액을 조회합니다."
    )
    @GetMapping("/balance")
    public ApiResponse<PointBalanceResponseDto> getPointBalance(@Auth Long memberId) {
        PointBalanceResponseDto pointBalanceResponseDto = pointQueryService.getPointBalance(memberId);
        return ApiResponse.onSuccess(pointBalanceResponseDto);
    }

    @Operation(
            summary = "포인트 사용 내역 조회 | by 지안/윤진수",
            description = "로그인한 사용자의 포인트 사용 내역을 최신순으로 조회합니다."
    )
    @GetMapping("/history")
    public ApiResponse<PagingPointHistoryResponseDto> getPointHistory(@Auth Long memberId, Pageable pageable) {
        PagingPointHistoryResponseDto pagingPointHistoryResponseDto = pointQueryService.getPointHistory(memberId, pageable);
        return ApiResponse.onSuccess(pagingPointHistoryResponseDto);
    }

    @Operation(
            summary = "포인트 사용(테스트용, 연동 X) | by 지안/윤진수",
            description = "로그인한 사용자가 특정 주문 상품에 대해 포인트를 사용합니다.  \n" +
                    "사용할 포인트 금액과 주문 상품 ID를 입력해야 합니다."
    )
    @PostMapping("/use/{amount}/orderProducts/{orderProductId}")
    public ApiResponse<?> usePoints(@Auth Long memberId, @PathVariable int amount, @PathVariable long orderProductId) {
        pointCommandService.usePoints(memberId, amount, orderProductId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "리뷰 작성 후 반품(테스트용, 연동 X) | by 지안/윤진수",
            description = "리뷰 포인트를 취소합니다.  \n" +
                    "반품 시 PointCommandService.cancelReviewPoint 호출하여 사용"
    )
    @PostMapping("/cancel/orderProducts/{orderProductId}")
    public ApiResponse<?> cancelReviewPoint(@PathVariable long orderProductId) {
        pointCommandService.cancelReviewPoint(orderProductId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "결제 시 사용된 포인트 환불(테스트용, 연동 X) | by 지안/윤진수",
            description = "사용자가 사용한 포인트를 환불합니다.  \n" +
                    "반품 시 PointCommandService.refundUsedPoints 호출하여 사용"
    )
    @PostMapping("/refund/orderProducts/{orderProductId}")
    public ApiResponse<?> refundUsedPoints(@PathVariable long orderProductId) {
        pointCommandService.refundUsedPoints(orderProductId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "리뷰 작성 후 포인트 추가(테스트용, 연동 X) | by 지안/윤진수",
            description = "리뷰 작성 후 포인트를 추가합니다.  \n" +
                    "리뷰 작성 시 PointCommandService.addReviewPoints 호출하여 사용"
    )
    @PostMapping("/add/{amount}/orderProducts/{orderProductId}")
    public ApiResponse<?> addReviewPoints(@Auth Long memberId, @PathVariable int amount, @PathVariable long orderProductId) {
        pointCommandService.addReviewPoints(memberId, amount, orderProductId);
        return ApiResponse.onSuccess(null);
    }
}
