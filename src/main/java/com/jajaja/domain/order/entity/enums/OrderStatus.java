package com.jajaja.domain.order.entity.enums;

public enum OrderStatus {
    READY,                  // 결제 대기
    IN_PROGRESS,            // 결제 승인 전 
    WAITING_FOR_DEPOSIT,    // 가상 계좌 입금 대기 중
    DONE,                   // 결제 승인
    CANCELED,               // 승인된 결제 취소
    ABORTED,                // 결제 승인 실패
    EXPIRED,                // 결제 유효 기간이 지나 거래 취소
    SHIPPING,               // 배송중
    DELIVERED,              // 배송 완료
    REFUND_REQUESTED,       // 환불 요청
    REFUND_FAILED,          // 환불 실패
    REFUNDED,               // 환불 완료
    TEAM_MATCHING_FAILED    // 팀 매칭 실패 (환불 대상)
}
