package com.jajaja.domain.order.entity;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.delivery.entity.Delivery;
import com.jajaja.domain.order.entity.enums.OrderType;
import com.jajaja.domain.order.entity.enums.PaymentMethod;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.user.entity.User;
import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`order`")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Integer discountAmount;

    @Column(nullable = false)
    private Integer pointUsedAmount;

    @Column(nullable = false)
    private Integer shippingFee;

    @Column(length = 10, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Team team;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public int calculateAmount() {
        return orderProducts.stream()
                .mapToInt(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    public int calculateFinalAmount() {
        return calculateAmount() - discountAmount - pointUsedAmount + shippingFee;
    }
}
