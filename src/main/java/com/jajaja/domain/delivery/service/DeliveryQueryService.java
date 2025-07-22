package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryResponseDto;

import java.util.List;

public interface DeliveryQueryService {
	List<DeliveryResponseDto> getDeliveriesByMemberId(Long memberId);
}
