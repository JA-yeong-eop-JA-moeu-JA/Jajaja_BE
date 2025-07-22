package com.jajaja.domain.order.repository;

import com.jajaja.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
