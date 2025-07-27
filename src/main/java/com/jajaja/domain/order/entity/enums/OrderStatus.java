package com.jajaja.domain.order.entity.enums;

public enum OrderStatus {
    PAYMENT_COMPLETED,      // 결제 완료
    PAYMENT_FAILED,         // 결제 실패
    PREPARING,              // 배송 준비 중
    SHIPPING,               // 배송중
    DELIVERED,              // 배송 완료
    CANCELLED,              // 주문 취소
    REFUNDED,               // 환불 완료
    TEAM_MATCHING_FAILED    // 팀 매칭 실패 (환불 대상)
}
