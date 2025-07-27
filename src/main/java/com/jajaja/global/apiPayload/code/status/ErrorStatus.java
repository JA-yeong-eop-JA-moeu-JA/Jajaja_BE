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

    // AUTH 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH4011", "리소스 접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4012", "액세스 토큰의 형식이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4013","액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH4014", "리프레쉬 토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4015", "리프레쉬 토큰의 형식이 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4016", "리프레쉬 토큰이 만료되었습니다. 다시 로그인해 주세요."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4017", "일치하지 않는 리프레시 토큰입니다."),

    // MEMBER 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    MEMBER_BUSINESS_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBERBUSINESS4002", "사용자의 업종 정보가 없습니다."),
    MEMBER_REQUIRED(HttpStatus.BAD_REQUEST, "MEMBER4003", "회원만 사용할 수 있는 기능입니다."),
    
    // DELIVERY 관련 에러
    DELIVERY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DELIVERY4001", "배송지가 없습니다."),
    DELIVERY_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "DELIVERY4002", "배송지의 주인과 현재 로그인한 사용자가 다릅니다."),

    // BUSINESS CATEGORY 관련 에러
    BUSINESS_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4001", "업종이 존재하지 않습니다."),
    BUSINESS_CATEGORY_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "CATEGORY4002", "이미 업종이 등록된 사용자입니다."),
    BUSINESS_CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST, "CATEGORY4003", "업종 정보가 필요합니다."),

    // CATEGORY 관련 에러
    INVALID_CATEGORY_GROUP(HttpStatus.BAD_REQUEST, "CATEGORY4004", "유효하지 않은 카테고리 그룹입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY4005", "존재하지 않는 카테고리입니다."),
    INVALID_PRODUCT_SORT_TYPE(HttpStatus.BAD_REQUEST, "CATEGORY4006", "정렬 기준이 올바르지 않습니다."),
    PRODUCTS_NOT_FOUND_IN_CATEGORY(HttpStatus.BAD_REQUEST, "CATEGORY4007", "해당 카테고리에 등록된 상품이 없습니다."),

    // PRODUCT 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT4001", "상품이 없습니다."),

    // SEARCH 관련 에러
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST, "SEARCH4001", "유효하지 않은 검색어입니다."),
    
    // OPTION 관련 에러
    OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "OPTION4001", "옵션이 없습니다."),

    // CART 관련 에러
    CART_NOT_FOUND(HttpStatus.BAD_REQUEST,  "CART4001", "장바구니가 없습니다."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "CART4002", "장바구니가 비어있습니다."),
    
    // CART PRODUCT 관련 에러
    CART_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CARTPRODUCT4001", "장바구니에 해당 상품이 없습니다."),
    
    // COUPON 관련 에러
    COUPON_NOT_FOUND(HttpStatus.BAD_REQUEST, "COUPON4001", "쿠폰이 존재하지 않습니다."),
    COUPON_NOT_OWNED(HttpStatus.BAD_REQUEST, "COUPON4002", "보유하지 않은 쿠폰입니다."),
    COUPON_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "COUPON4003", "사용할 수 없는 쿠폰입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "COUPON4004", "만료된 쿠폰입니다."),
    COUPON_MIN_ORDER_AMOUNT_NOT_MET(HttpStatus.BAD_REQUEST, "COUPON4005", "최소 주문 금액을 충족하지 않습니다."),
    COUPON_BRAND_CONDITION_NOT_MET(HttpStatus.BAD_REQUEST, "COUPON4006", "해당 브랜드 상품이 장바구니에 없습니다."),
    COUPON_CATEGORY_CONDITION_NOT_MET(HttpStatus.BAD_REQUEST, "COUPON4007", "해당 카테고리 상품이 장바구니에 없습니다."),
    INVALID_COUPON_TYPE(HttpStatus.BAD_REQUEST, "COUPON4009", "유효하지 않은 쿠폰 타입입니다."),
  
    // TEAM 관련 에러
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM4001", "팀이 없습니다."),
    TEAM_ALREADY_HAS_MEMBER(HttpStatus.BAD_REQUEST, "TEAM4002", "이미 팀에 멤버가 존재합니다."),
    CANNOT_JOIN_OWN_TEAM(HttpStatus.BAD_REQUEST, "TEAM4003", "자신이 생성한 팀에는 참여할 수 없습니다."),
  
    // ORDER 관련 에러
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER4001", "주문이 없습니다."),
    ORDER_NOT_REFUNDABLE(HttpStatus.BAD_REQUEST, "ORDER4002", "환불할 수 없는 주문 상태입니다."),
    ORDER_ALREADY_REFUNDED(HttpStatus.BAD_REQUEST, "ORDER4003", "이미 환불된 주문입니다."),
    
    // 환불 관련 에러
    REFUND_FAILED(HttpStatus.BAD_REQUEST, "REFUND4001", "환불 처리에 실패했습니다."),

    // ORDER PRODUCT 관련 에러
    ORDER_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDERPRODUCT4001", "주문 상품이 없습니다."),

    // REVIEW 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW4001", "리뷰가 없습니다."),
    REVIEW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "REVIEW4002", "해당 상품에 대한 리뷰를 작성할 수 없습니다."),
    REVIEW_TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "REVIEW4003", "이미지는 최대 5장까지 등록할 수 있습니다."),
    REVIEW_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "REVIEW4004", "본인의 리뷰만 삭제할 수 있습니다."),

    // NOTIFICATION 관련 에러
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTIFICATION4001", "알림이 없습니다."),
    NOTIFICATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "NOTIFICATION4031", "해당 알림에 대한 접근 권한이 없습니다."),

    // S3 관련 에러
    INVALID_IMAGE_KEY(HttpStatus.BAD_REQUEST, "IMAGE4001", "유효하지 않은 이미지 키입니다."),

    // 포인트 관련 에러
    POINT_NOT_FOUND(HttpStatus.BAD_REQUEST, "POINT4001", "포인트가 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "POINT4002", "포인트가 부족합니다."),
    ALREADY_CANCELLED_POINT(HttpStatus.BAD_REQUEST, "POINT4003", "이미 취소된 포인트입니다."),
    ALREADY_REFUNDED_POINT(HttpStatus.BAD_REQUEST, "POINT4004", "이미 환불된 포인트입니다."),

    // 결제 관련 에러
    PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4001", "결제 정보를 찾을 수 없습니다."),
    PAYMENT_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "PAYMENT4002", "결제가 완료되지 않았습니다."),
    PAYMENT_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "PAYMENT4003", "결제 검증에 실패했습니다."),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT4004", "결제 금액이 일치하지 않습니다."),
    PAYMENT_MERCHANT_UID_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT4005", "결제 고유번호가 일치하지 않습니다."),;

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