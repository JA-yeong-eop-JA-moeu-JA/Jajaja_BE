package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.request.OrderApproveRequestDto;
import com.jajaja.domain.order.dto.request.OrderPrepareRequestDto;
import com.jajaja.domain.order.dto.request.OrderRefundRequestDto;
import com.jajaja.domain.order.dto.response.OrderApproveResponseDto;
import com.jajaja.domain.order.dto.response.OrderPrepareResponseDto;
import com.jajaja.domain.order.dto.response.OrderRefundResponseDto;

public interface OrderCommandService {
    OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request);
    OrderApproveResponseDto approveOrder(Long memberId, OrderApproveRequestDto request);
    OrderRefundResponseDto refundOrder(Long memberId, OrderRefundRequestDto request);
}