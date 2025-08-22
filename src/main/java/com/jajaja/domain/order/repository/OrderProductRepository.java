package com.jajaja.domain.order.repository;

import com.jajaja.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @EntityGraph(attributePaths = {"product"})
    Optional<OrderProduct> findById(Long orderProductId);
}
