package com.jajaja.domain.delivery.dto;

import com.jajaja.domain.delivery.entity.Delivery;
import lombok.Builder;

@Builder
public record DeliveryResponseDto(
		Long id,
		String name,
		String phone,
		String address,
		String addressDetail,
		String zipcode,
		String buildingPassword,
		Boolean isDefault
) {
	public static DeliveryResponseDto of(Delivery delivery) {
		return DeliveryResponseDto.builder()
				.id(delivery.getId())
				.name(delivery.getName())
				.phone(delivery.getPhone())
				.address(delivery.getAddress())
				.addressDetail(delivery.getAddressDetail())
				.zipcode(delivery.getZipcode())
				.buildingPassword(delivery.getBuildingPassword())
				.isDefault(delivery.getIsDefault())
				.build();
	}
}
