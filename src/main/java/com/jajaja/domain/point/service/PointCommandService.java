package com.jajaja.domain.point.service;

public interface PointCommandService {

    void usePoints(Long memberId, int amountToUse, long orderProductId);

    void addReviewPoints(Long memberId, int amount, long orderProductId);

    void cancelReviewPoint(Long orderProductId);

    void refundUsedPoints(Long orderProductId);
}
