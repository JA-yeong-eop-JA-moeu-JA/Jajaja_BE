package com.jajaja.global.apiPayload.exception.custom;

import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class TossPaymentException extends GeneralException { // GeneralException을 상속받도록 함
	
	public TossPaymentException(final ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
