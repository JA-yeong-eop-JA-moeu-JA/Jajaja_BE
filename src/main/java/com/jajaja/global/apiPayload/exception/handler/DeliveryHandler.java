package com.jajaja.global.apiPayload.exception.handler;

import com.jajaja.global.apiPayload.code.BaseErrorCode;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class DeliveryHandler extends GeneralException {
	public DeliveryHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}