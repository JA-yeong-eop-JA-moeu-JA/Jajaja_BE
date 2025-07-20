package com.jajaja.domain.coupon.entity;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
import com.jajaja.domain.coupon.entity.enums.DiscountType;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType discountType;

    @Column(nullable = false)
    private Integer discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConditionType conditionType;

    @Column(length = 100)
    private String conditionValues;

    @Column
    private Integer minOrderAmount;

    @Column
    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "coupon")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> memberCoupons = new ArrayList<>();

    @OneToOne(mappedBy = "coupon", fetch = FetchType.LAZY)
    private Cart cart;

}