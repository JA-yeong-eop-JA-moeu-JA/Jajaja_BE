package com.jajaja.domain.order.repository;

import com.jajaja.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findByOrderMemberIdAndProductId(Long memberId, Long productId);
}
