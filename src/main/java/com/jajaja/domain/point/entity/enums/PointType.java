package com.jajaja.domain.point.entity.enums;

public enum PointType {
    REVIEW,
    USE,
    EXPIRED,
    CANCEL,
    REFUND,
    SHARE,
    FIRST_PURCHASE;

    public boolean isEarnType() {
        return this == PointType.REVIEW || this == PointType.SHARE || this == PointType.FIRST_PURCHASE;
    }
}
