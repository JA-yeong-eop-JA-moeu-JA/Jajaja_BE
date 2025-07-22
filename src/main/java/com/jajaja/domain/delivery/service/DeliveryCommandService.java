package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryAddRequestDto;

public interface DeliveryCommandService {
	void addDeliveryAddress(Long memberId, DeliveryAddRequestDto request);
	void deleteDeliveryAddress(Long memberId,  Long deliveryId);
}
