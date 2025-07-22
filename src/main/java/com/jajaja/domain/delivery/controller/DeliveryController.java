package com.jajaja.domain.delivery.controller;

import com.jajaja.domain.delivery.dto.DeliveryResponseDto;
import com.jajaja.domain.delivery.service.DeliveryQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryController {
	
	private final DeliveryQueryService deliveryQueryService;
	
	@Operation(
			summary = "배송지 목록 조회 API | by 엠마/신윤지",
			description = "배송지 목록을 조회합니다.")
	@GetMapping("/")
	public ApiResponse<List<DeliveryResponseDto>> getDeliveriesByMemberId(@Auth Long memberId) {
		return ApiResponse.onSuccess(deliveryQueryService.getDeliveriesByMemberId(memberId));
	}
}
