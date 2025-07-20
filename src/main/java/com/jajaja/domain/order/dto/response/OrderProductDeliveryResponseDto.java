package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.OrderProduct;

public record OrderProductDeliveryResponseDto(
        String invoiceNumber
) {
    public static OrderProductDeliveryResponseDto from(OrderProduct orderProduct) {
        return new OrderProductDeliveryResponseDto(orderProduct.getInvoiceNumber());
    }
}
