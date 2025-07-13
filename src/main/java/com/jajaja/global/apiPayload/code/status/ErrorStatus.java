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
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT4001", "상품이 없습니다."),

    // TEAM 관련 에러
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM4001", "팀이 없습니다."),
    TEAM_ALREADY_HAS_MEMBER(HttpStatus.BAD_REQUEST, "TEAM4002", "이미 팀에 멤버가 존재합니다."),
    CANNOT_JOIN_OWN_TEAM(HttpStatus.BAD_REQUEST, "TEAM4003", "자신이 생성한 팀에는 참여할 수 없습니다.");

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