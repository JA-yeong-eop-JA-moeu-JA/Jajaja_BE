package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.request.OrderCreateRequestDto;
import com.jajaja.domain.order.dto.request.OrderPrepareRequestDto;
import com.jajaja.domain.order.dto.request.OrderRefundRequestDto;
import com.jajaja.domain.order.dto.response.OrderCreateResponseDto;
import com.jajaja.domain.order.dto.response.OrderPrepareResponseDto;
import com.jajaja.domain.order.dto.response.OrderRefundResponseDto;

public interface OrderCommandService {
    OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request);
    OrderCreateResponseDto createOrder(Long memberId, OrderCreateRequestDto request);
    OrderRefundResponseDto refundOrder(Long memberId, OrderRefundRequestDto request);
}