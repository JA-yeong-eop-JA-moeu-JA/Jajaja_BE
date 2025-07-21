package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.response.OrderProductDeliveryResponseDto;
import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.order.repository.OrderProductRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductQueryServiceImpl implements OrderProductQueryService {

    private final OrderProductRepository orderProductRepository;

    @Override
    public OrderProductDeliveryResponseDto getDelivery(Long orderProductId) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_PRODUCT_NOT_FOUND));
        return OrderProductDeliveryResponseDto.from(orderProduct);
    }
}
