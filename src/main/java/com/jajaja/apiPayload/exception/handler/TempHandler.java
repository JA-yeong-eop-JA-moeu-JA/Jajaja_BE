package com.jajaja.apiPayload.exception.handler;

import com.jajaja.apiPayload.code.BaseErrorCode;
import com.jajaja.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
