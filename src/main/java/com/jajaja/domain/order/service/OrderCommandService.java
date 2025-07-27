package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.request.OrderCreateRequestDto;
import com.jajaja.domain.order.dto.request.OrderPrepareRequestDto;
import com.jajaja.domain.order.dto.response.OrderCreateResponseDto;
import com.jajaja.domain.order.dto.response.OrderPrepareResponseDto;

public interface OrderCommandService {
    OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request);
    OrderCreateResponseDto createOrder(Long memberId, OrderCreateRequestDto request);
}