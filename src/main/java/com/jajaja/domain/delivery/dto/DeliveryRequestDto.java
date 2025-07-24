package com.jajaja.domain.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeliveryRequestDto(
		@NotBlank(message = "이름은 필수입니다.")
		@Size(max = 10)
		String name,
		
		@NotBlank(message = "전화번호는 필수입니다.")
		@Size(min = 13, max = 13)
		String phone,
		
		@NotBlank(message = "주소는 필수입니다.")
		String address,
		
		String addressDetail,
		
		@NotBlank(message = "우편번호는 필수입니다.")
		@Size(min = 5, max = 5)
		String zipcode,
		
		String buildingPassword,
		
		@NotNull(message = "기본 배송지 여부는 필수입니다.")
		Boolean isDefault
) {
}
