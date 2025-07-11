package com.jajaja.global.apiPayload.exception.custom;

import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.GeneralException;

public class BadRequestException extends GeneralException {
    public BadRequestException(final ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
