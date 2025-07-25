package com.jajaja.global.apiPayload.exception.handler;

import com.jajaja.global.apiPayload.code.BaseErrorCode;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class CouponHandler extends GeneralException {
	
	public CouponHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
