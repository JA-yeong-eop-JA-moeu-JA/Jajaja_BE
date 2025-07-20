package com.jajaja.domain.cart.entity;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.user.entity.Member;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cart extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();
    
    /**
     *  Cart와 CartProduct의 양방향 관계를 설정합니다.
     */
    public void addCartProduct(CartProduct cartProduct) {
        this.cartProducts.add(cartProduct);
        cartProduct.setCart(this);
    }
    
    /**
     *  Cart에서 특정 CartProduct를 제거합니다.
     */
    public void deleteCartProduct(Long productId, Long optionId) {
        CartProduct product = this.cartProducts.stream()
                .filter(cp -> cp.getProduct().getId().equals(productId))
                .filter(cp ->  Objects.equals(((cp.getProductOption() != null) ? cp.getProductOption().getId() : null), optionId))
                .findFirst().orElseThrow(() -> new BadRequestException(ErrorStatus.CART_PRODUCT_NOT_FOUND));
        this.cartProducts.remove(product);
    }

    /**
     *  Cart에서 한 Product에 대한 CartProduct를 모두 제거합니다.
     */
    public void deleteAllCartProductsByProductId(Long productId) {
        List<CartProduct> toRemove = this.cartProducts.stream()
                .filter(cp -> cp.getProduct().getId().equals(productId))
                .toList();

        if (toRemove.isEmpty()) {
            throw new BadRequestException(ErrorStatus.CART_PRODUCT_NOT_FOUND);
        }

        this.cartProducts.removeAll(toRemove);
    }
}