package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.response.OrderDetailResponseDto;

public interface OrderQueryService {

    OrderDetailResponseDto getOrderById(Long orderId);
}
