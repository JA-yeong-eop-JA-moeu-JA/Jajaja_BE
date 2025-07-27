package com.jajaja.domain.order.controller;

import com.jajaja.domain.order.dto.request.OrderCreateRequestDto;
import com.jajaja.domain.order.dto.request.OrderPrepareRequestDto;
import com.jajaja.domain.order.dto.response.OrderCreateResponseDto;
import com.jajaja.domain.order.dto.response.OrderDetailResponseDto;
import com.jajaja.domain.order.dto.response.OrderPrepareResponseDto;
import com.jajaja.domain.order.dto.response.PagingOrderListResponseDto;
import com.jajaja.domain.order.service.OrderCommandService;
import com.jajaja.domain.order.service.OrderQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @PostMapping("/prepare")
    @Operation(summary = "결제 준비 API | by 엠마/신윤지", description = "결제 전 주문 데이터를 검증하고 merchant_uid를 생성합니다.")
    public ApiResponse<OrderPrepareResponseDto> prepareOrder(
            @Auth Long memberId,
            @Valid @RequestBody OrderPrepareRequestDto request) {
        return ApiResponse.onSuccess(orderCommandService.prepareOrder(memberId, request));
    }

    @PostMapping
    @Operation(summary = "결제 검증 API | by 엠마/신윤지", description = "결제를 검증하고 주문 데이터를 생성합니다.")
    public ApiResponse<OrderCreateResponseDto> createOrder(
            @Auth Long memberId,
            @Valid @RequestBody OrderCreateRequestDto request) {
        return ApiResponse.onSuccess(orderCommandService.createOrder(memberId, request));
    }

    @Operation(
            summary = "내 주문 목록 조회 API | by 지안/윤진수",
            description = "사용자의 모든 주문 목록을 조회합니다."
    )
    @GetMapping("/me")
    public ApiResponse<PagingOrderListResponseDto> getMyOrders(@Auth Long userId, Pageable pageable) {
        PagingOrderListResponseDto pagingOrderListResponseDto = orderQueryService.getMyOrders(userId, pageable);
        return ApiResponse.onSuccess(pagingOrderListResponseDto);
    }

    @Operation(
            summary = "주문 상세 조회 API | by 지안/윤진수",
            description = "주문 상세 페이지를 조회합니다."
    )
    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderDetailResponseDto orderDetailResponseDto = orderQueryService.getOrderById(orderId);
        return ApiResponse.onSuccess(orderDetailResponseDto);
    }
}
