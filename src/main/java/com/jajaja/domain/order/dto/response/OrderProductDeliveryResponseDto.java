package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;
import lombok.Builder;

@Builder
public record OrderProductDeliveryResponseDto(
        String invoiceNumber,
        String courier,
        OrderDeliveryDto delivery
) {
    public static OrderProductDeliveryResponseDto from(OrderProduct orderProduct) {
        return OrderProductDeliveryResponseDto.builder()
                .invoiceNumber(orderProduct.getInvoiceNumber())
                .courier("CJ대한통운")
                .delivery(OrderDeliveryDto.from(orderProduct.getOrder().getDelivery()))
                .build();
    }
}
