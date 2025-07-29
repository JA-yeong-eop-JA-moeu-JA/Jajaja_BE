package com.jajaja.domain.point.repository;

import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point, Integer>, PointRepositoryCustom {
    
    @Query("""
                SELECT COALESCE(SUM(
                    CASE
                        WHEN p.type IN ('REVIEW', 'REFUND') THEN p.amount
                        WHEN p.type IN ('USE', 'EXPIRED', 'CANCEL') THEN -p.amount
                    END
                ), 0)
                FROM Point p
                WHERE p.member.id = :memberId
            """)
    int findPointBalanceByMemberId(@Param("memberId") Long memberId);
    
    boolean existsByTypeAndOrderProduct(PointType type, OrderProduct orderProduct);
    
    
}