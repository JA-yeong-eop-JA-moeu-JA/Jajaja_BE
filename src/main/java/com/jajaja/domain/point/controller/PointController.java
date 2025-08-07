package com.jajaja.domain.point.controller;

import com.jajaja.domain.point.dto.response.PagingPointHistoryResponseDto;
import com.jajaja.domain.point.dto.response.PointBalanceResponseDto;
import com.jajaja.domain.point.service.PointCommandService;
import com.jajaja.domain.point.service.PointQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public ApiResponse<PagingPointHistoryResponseDto> getPointHistory(
            @Auth Long memberId,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size) {
        PagingPointHistoryResponseDto pagingPointHistoryResponseDto = pointQueryService.getPointHistory(memberId, PageRequest.of(page, size));
        return ApiResponse.onSuccess(pagingPointHistoryResponseDto);
    }
}
