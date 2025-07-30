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
     * CartProduct 엔티티를 생성하고 가격을 계산합니다.
     * @param cart     업데이트 할 옵션
     * @param product     업데이트 할 옵션
     */
    public static CartProduct create(Cart cart, Product product, ProductOption option, int quantity) {
        int unitPrice = (option != null) ? option.getPrice() : product.getPrice();
        return CartProduct.builder()
                .cart(cart)
                .product(product)
                .productOption(option)
                .quantity(quantity)
                .totalPrice(unitPrice * quantity)
                .build();
    }
    
    /**
     * 장바구니 상품의 수량과 옵션을 업데이트합니다.
     * @param newOption     업데이트 할 옵션
     * @param newQuantity   업데이트 할 수량
     */
    public void update(ProductOption newOption, int newQuantity) {
        this.productOption = newOption;
        this.quantity = newQuantity;
        this.recalculateTotalPrice();
    }
    
    /**
     * totalPrice를 계산합니다.
     */
    private void recalculateTotalPrice() {
        int unitPrice = (this.productOption != null) ? this.productOption.getPrice() : this.product.getPrice();
        this.totalPrice = unitPrice * this.quantity;
    }
    
    /**
     * 단위가격을 반환합니다.
     */
    public int getUnitPrice() {
        return (this.productOption != null) ? this.productOption.getPrice() : this.product.getPrice();
    }
    
    /**
     *  Cart에서 CartProduct를 추가할 때 부모가 누구인지 알려주기 위해 호출하는 연관관계 편의 메소드입니다.
     */
    protected void setCart(Cart cart) {
        this.cart = cart;
    }
}