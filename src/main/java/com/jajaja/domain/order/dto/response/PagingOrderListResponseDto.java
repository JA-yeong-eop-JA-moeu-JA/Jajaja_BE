package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingOrderListResponseDto(
        PageResponse page,
        List<OrderListDto> orders
) {
    public static PagingOrderListResponseDto of(Page<Order> orderPage, List<OrderListDto> orderListDtos) {
        return PagingOrderListResponseDto.builder()
                .page(PageResponse.from(orderPage))
                .orders(orderListDtos)
                .build();
    }
}
