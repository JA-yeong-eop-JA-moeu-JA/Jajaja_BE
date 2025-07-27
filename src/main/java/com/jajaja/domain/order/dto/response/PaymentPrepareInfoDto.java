package com.jajaja.domain.order.dto.response;

import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaymentPrepareInfoDto {
    private String productNames;
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;

    public static PaymentPrepareInfoDto of(Member member, List<CartProduct> cartProducts) {
        return PaymentPrepareInfoDto.builder()
                .productNames(generateProductNames(cartProducts))
                .buyerName(member.getName())
                .buyerEmail(member.getEmail())
                .buyerTel(member.getPhone())
                .build();
    }

    private static String generateProductNames(List<CartProduct> cartProducts) {
        if (cartProducts.isEmpty()) {
            return "상품";
        }
        
        String firstName = cartProducts.get(0).getProduct().getName();
        if (cartProducts.size() == 1) {
            return firstName;
        }
        
        return firstName + " 외 " + (cartProducts.size() - 1) + "건";
    }
}