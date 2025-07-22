package com.jajaja.domain.order.controller;

import com.jajaja.domain.order.dto.response.OrderProductDeliveryResponseDto;
import com.jajaja.domain.order.service.OrderProductQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-products")
public class OrderProductsController {

    private final OrderProductQueryService orderProductQueryService;

    @Operation(
            summary = "배송 조회 API | by 지안/윤진수",
            description = "주문 상품의 배송 정보를 조회합니다."
    )
    @GetMapping("/{orderProductId}/delivery")
    public ApiResponse<OrderProductDeliveryResponseDto> getDelivery(@PathVariable Long orderProductId) {
        OrderProductDeliveryResponseDto orderProductDeliveryResponseDto = orderProductQueryService.getDelivery(orderProductId);
        return ApiResponse.onSuccess(orderProductDeliveryResponseDto);
    }
}
