package com.jajaja.domain.order.dto.response.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EasyPayDto(
		String provider,
		int amount,
		int discountAmount
) {}