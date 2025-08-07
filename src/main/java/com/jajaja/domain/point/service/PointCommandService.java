package com.jajaja.domain.point.service;

import com.jajaja.domain.order.entity.Order;

public interface PointCommandService {
    
    void usePoints(Long memberId, Order order);
    
    void addReviewPoints(Long memberId, int amount, long reviewId);
    
    void refundUsedPoints(Long orderId);
}