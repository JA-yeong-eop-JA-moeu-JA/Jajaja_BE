package com.jajaja.domain.point.repository;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.product.entity.Product;
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
    
    boolean existsByTypeAndOrder(PointType type, Order order);

    boolean existsByMemberAndType(Member member, PointType type);

    boolean existsByMemberAndTypeAndProduct(Member member, PointType type, Product product);
}