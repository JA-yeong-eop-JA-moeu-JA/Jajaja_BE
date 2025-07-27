package com.jajaja.domain.point.repository;

import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Integer>, PointRepositoryCustom {

    boolean existsByTypeAndOrderProduct(PointType type, OrderProduct orderProduct);

}
