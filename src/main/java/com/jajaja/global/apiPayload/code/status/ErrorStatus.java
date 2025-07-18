package com.jajaja.global.apiPayload.code.status;

import com.jajaja.global.apiPayload.code.BaseErrorCode;
import com.jajaja.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // USER 관련 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    
    // BUSINESS CATEGORY 관련 에러
    BUSINESS_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4001", "업종이 존재하지 않습니다."),
    BUSINESS_CATEGORY_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "CATEGORY4002", "이미 업종이 등록된 사용자입니다."),
    
    // PRODUCT 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT4001", "제품이 없습니다."),
    
    // OPTION 관련 에러
    OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "OPTION4001", "옵션이 없습니다."),

    // CART 관련 에러
    CART_NOT_FOUND(HttpStatus.BAD_REQUEST,  "CART4001", "장바구니기 없습니다."),
    
    // CART PRODUCT 관련 에러
    CART_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CARTPRODUCT4001", "장바구니에 해당 상품이 없습니다."),

    // ORDER 관련 에러
     ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4001", "주문이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}