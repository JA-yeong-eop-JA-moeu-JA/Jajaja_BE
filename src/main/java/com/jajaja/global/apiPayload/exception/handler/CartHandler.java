package com.jajaja.global.apiPayload.exception.handler;

import com.jajaja.global.apiPayload.code.BaseErrorCode;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class CartHandler extends GeneralException {
	
	public CartHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
	
}
