package com.jajaja.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentFailureDto(
		String code,
		String message
) {}