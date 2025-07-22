package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryRequestDto;

public interface DeliveryCommandService {
	void addDeliveryAddress(Long memberId, DeliveryRequestDto request);
	void deleteDeliveryAddress(Long memberId,  Long deliveryId);
}
