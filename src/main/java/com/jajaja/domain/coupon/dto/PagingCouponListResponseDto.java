package com.jajaja.domain.coupon.dto;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingCouponListResponseDto(
        PageResponse page,
        List<CouponResponseDto> coupons
) {
    public static PagingCouponListResponseDto of(Page<?> couponPage, List<CouponResponseDto> couponDtos) {
        return PagingCouponListResponseDto.builder()
                .page(PageResponse.from(couponPage))
                .coupons(couponDtos)
                .build();
    }
}