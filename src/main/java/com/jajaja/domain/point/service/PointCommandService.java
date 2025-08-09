package com.jajaja.domain.point.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.order.entity.Order;

public interface PointCommandService {
    
    void usePoints(Long memberId, Order order);
    
    void addReviewPoints(Long memberId, long reviewId);
    
    void refundUsedPoints(Long orderId);

    void addFirstPurchasePointsIfPossible(Member member);

    void addSharePoint(Long memberId, Long productId);
}