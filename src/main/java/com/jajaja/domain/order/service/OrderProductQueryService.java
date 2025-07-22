package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.response.OrderProductDeliveryResponseDto;

public interface OrderProductQueryService {

    OrderProductDeliveryResponseDto getDelivery(Long orderProductId);
}
