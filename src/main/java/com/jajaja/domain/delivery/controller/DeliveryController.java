package com.jajaja.domain.delivery.controller;

import com.jajaja.domain.delivery.dto.DeliveryAddRequestDto;
import com.jajaja.domain.delivery.dto.DeliveryResponseDto;
import com.jajaja.domain.delivery.service.DeliveryCommandService;
import com.jajaja.domain.delivery.service.DeliveryQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class DeliveryController {
	
	private final DeliveryQueryService deliveryQueryService;
	private final DeliveryCommandService deliveryCommandService;
	
	@Operation(
			summary = "배송지 목록 조회 API | by 엠마/신윤지",
			description = "배송지 목록을 조회합니다.")
	@GetMapping("/")
	public ApiResponse<List<DeliveryResponseDto>> getDeliveriesByMemberId(@Auth Long memberId) {
		return ApiResponse.onSuccess(deliveryQueryService.getDeliveriesByMemberId(memberId));
	}
	
	@Operation(
			summary = "배송지 추가 API | by 엠마/신윤지",
			description = "배송지를 새로 추가합니다."
	)
	@PostMapping("/")
	public ApiResponse<String> addDeliveryAddress(@Auth Long memberId, @Valid DeliveryAddRequestDto request) {
		deliveryCommandService.addDeliveryAddress(memberId, request);
		return ApiResponse.onSuccess("성공적으로 배송지를 추가하였습니다.");
	}
	
	@Operation(
			summary = "배송지 삭제 API | by 엠마/신윤지",
			description = "기존에 저장되어 있던 배송지를 삭제합니다."
	)
	@DeleteMapping("/")
	public ApiResponse<String> deleteDeliveryAddress(@Auth Long memberId, @RequestParam Long deliveryId) {
		deliveryCommandService.deleteDeliveryAddress(memberId, deliveryId);
		return ApiResponse.onSuccess("성공적으로 배송지를 삭제하였습니다.");
	}
}
