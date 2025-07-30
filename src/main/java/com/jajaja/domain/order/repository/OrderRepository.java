package com.jajaja.domain.order.repository;

import com.jajaja.domain.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @EntityGraph(attributePaths = {"orderProducts", "orderProducts.product", "orderProducts.productOption", "team", "delivery"})
    Optional<Order> findById(Long id);

    Optional<Order> findByMerchantUid(String merchantUid);
}
