package com.jajaja.domain.cart.entity;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberCoupon;
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
	@JoinColumn(name = "member_coupon_id")
	private MemberCoupon memberCoupon;
    
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

    /**
     * Cart에 쿠폰을 적용합니다.
     */
	public void applyCoupon(MemberCoupon memberCoupon) {
		this.memberCoupon = memberCoupon;
	}
	
	/**
     * Cart에서 쿠폰을 제거합니다.
     */
	public void removeCoupon() {
		this.memberCoupon = null;
	}
	
    /**
     * 장바구니의 총 금액을 계산합니다.
     */
    public int calculateTotalAmount() {
        return this.cartProducts.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
    }
	
	/**
	 * 적용된 쿠폰 정보를 가져옵니다.
	 */
	public Coupon getCoupon() {
		return this.memberCoupon != null ? this.memberCoupon.getCoupon() : null;
	}
}