package com.jajaja.domain.point.repository;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Integer>, PointRepositoryCustom {
    
    boolean existsByTypeAndOrder(PointType type, Order order);

    boolean existsByMemberAndType(Member member, PointType type);

    boolean existsByMemberAndTypeAndProduct(Member member, PointType type, Product product);
}