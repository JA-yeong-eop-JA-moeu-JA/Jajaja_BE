package com.jajaja.domain.order.controller;

import com.jajaja.domain.order.dto.response.OrderDetailResponseDto;
import com.jajaja.domain.order.dto.response.PagingOrderListResponseDto;
import com.jajaja.domain.order.service.OrderQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderQueryService orderQueryService;

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
