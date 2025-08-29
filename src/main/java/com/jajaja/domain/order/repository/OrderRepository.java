package com.jajaja.domain.order.repository;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @EntityGraph(attributePaths = {"orderProducts", "orderProducts.product", "orderProducts.productOption", "team", "delivery"})
    Optional<Order> findById(Long id);
    Optional<Order> findByOrderId(String orderId);
    Boolean existsByMember(Member member);
    List<Order> findOrdersByOrderStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime createdAtBefore);
}
