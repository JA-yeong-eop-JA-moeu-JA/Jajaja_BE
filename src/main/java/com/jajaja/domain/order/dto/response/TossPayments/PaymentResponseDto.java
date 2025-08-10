package com.jajaja.domain.order.dto.response.TossPayments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentResponseDto(
		String mId,
		String lastTransactionKey,
		String paymentKey,
		String orderId,
		String orderName,
		int taxExemptionAmount,
		String status,
		String requestedAt,
		String approvedAt,
		boolean useEscrow,
		boolean cultureExpense,
		CardDto card,
		String virtualAccount,
		String transfer,
		String mobilePhone,
		String giftCertificate,
		String cashReceipt,
		List<String> cashReceipts,
		Object discount,
		List<CancelDto> cancels,
		String secret,
		String type,
		EasyPayDto easyPay,
		String country,
		PaymentFailureDto failure,
		boolean isPartialCancelable,
		Receipt receipt,
		Checkout checkout,
		String currency,
		int totalAmount,
		int balanceAmount,
		int suppliedAmount,
		int vat,
		int taxFreeAmount,
		Object metadata,
		String method,
		String version
) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Receipt(String url) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Checkout(String url) {}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record CancelDto(
			String cancelReason,
			String canceledAt,
			int cancelAmount,
			int taxFreeAmount,
			int taxExemptionAmount,
			int refundableAmount,
			int transferDiscountAmount,
			int easyPayDiscountAmount,
			String transactionKey,
			String receiptKey,
			String cancelStatus,
			String cancelRequestId
	) {}
}