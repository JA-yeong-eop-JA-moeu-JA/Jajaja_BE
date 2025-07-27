package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderPrepareResponseDto {
    private String merchantUid;
    private Integer totalAmount;
    private Integer discountAmount;
    private Integer pointDiscount;
    private Integer shippingFee;
    private Integer finalAmount;
    private PaymentPrepareInfoDto paymentInfo;

    public static OrderPrepareResponseDto of(String merchantUid, int totalAmount, int discountAmount, 
                                           int pointDiscount, int shippingFee, int finalAmount,
                                           Member member, List<CartProduct> cartProducts) {
        return OrderPrepareResponseDto.builder()
                .merchantUid(merchantUid)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .pointDiscount(pointDiscount)
                .shippingFee(shippingFee)
                .finalAmount(finalAmount)
                .paymentInfo(PaymentPrepareInfoDto.of(member, cartProducts))
                .build();
    }
}