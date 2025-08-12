package com.jajaja.domain.point.repository;

import com.jajaja.domain.point.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PointRepositoryCustom {
    
    Page<Point> findByMemberId(Long memberId, Pageable pageable);
    
    List<Point> findExpiringPoints();
    
    List<Point> findValidPointsOrderedByOldest(Long memberId, LocalDate today);
    
    Optional<Point> findReviewPointByOrderId(Long orderId);
    
    Optional<Point> findUsePointByOrderId(Long orderId);
}