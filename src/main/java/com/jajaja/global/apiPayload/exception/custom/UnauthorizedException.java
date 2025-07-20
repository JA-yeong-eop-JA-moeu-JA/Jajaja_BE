package com.jajaja.global.apiPayload.exception.custom;

import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class UnauthorizedException extends GeneralException {
    public UnauthorizedException(final ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
