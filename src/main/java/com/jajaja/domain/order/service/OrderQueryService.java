package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.response.OrderDetailResponseDto;
import com.jajaja.domain.order.dto.response.PagingOrderListResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderQueryService {

    PagingOrderListResponseDto getMyOrders(Long memberId, Pageable pageable);

    OrderDetailResponseDto getOrderById(Long orderId);
}
