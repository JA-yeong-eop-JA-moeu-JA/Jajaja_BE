package com.jajaja.domain.cart.entity;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductOption;
import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;
    
    /**
     * 상품 수량을 업데이트하고 totalPrice를 다시 계산합니다.
     *
     * @param quantity  변경한 수량
     */
    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
        int unitPrice = (this.productOption != null) ? this.getProductOption().getPrice() : this.getProduct().getPrice();
        this.totalPrice = unitPrice * quantity; // TODO 쿠폰 할인가 적용 로직 구현 필요
    }
    
    /**
     * 옵션을 변경합니다.
     *
     * @param productOption  변경한 옵션
     */
    public void updateOption(ProductOption productOption) {
        this.productOption = productOption;
        this.totalPrice = (productOption != null) ? productOption.getPrice() * this.quantity : this.product.getPrice() * this.quantity;
    }
    
    protected void setCart(Cart cart) {
        this.cart = cart;
    }
}
