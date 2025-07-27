package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountInfoDto {
    private Integer couponDiscount;
    private Integer pointUsed;
    private Integer totalDiscount;

    public static DiscountInfoDto of(Order order) {
        return DiscountInfoDto.builder()
                .couponDiscount(order.getDiscountAmount())
                .pointUsed(order.getPointUsedAmount())
                .totalDiscount(order.getDiscountAmount() + order.getPointUsedAmount())
                .build();
    }
}