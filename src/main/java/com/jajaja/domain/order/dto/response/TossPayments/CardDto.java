package com.jajaja.domain.order.dto.response.TossPayments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CardDto(
		String issuerCode,
		String acquirerCode,
		String number,
		int installmentPlanMonths,
		boolean isInterestFree,
		String interestPayer,
		String approveNo,
		boolean useCardPoint,
		String cardType,
		String ownerType,
		String acquireStatus,
		int amount
) {}