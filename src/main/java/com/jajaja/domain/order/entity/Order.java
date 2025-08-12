package com.jajaja.domain.order.entity;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.delivery.entity.Delivery;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.order.entity.enums.OrderType;
import com.jajaja.domain.order.entity.enums.PaymentMethod;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.member.entity.Member;
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
@Table(name = "`order`")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    private String orderId; // 결제 시 사용하는 오더 아이디

    @Column(length = 500)
    private String orderName; // 결제할 때 사용할 결제 건 명
    
    @Column(length = 200)
    private String paymentKey; // 결제 승인 시 사용하는 페이먼트 키
    
    @Column(nullable = false)
    private Integer discountAmount;
    
    @Column(nullable = false)
    private Integer pointUsedAmount;
    
    @Column(nullable = false)
    private Integer shippingFee;
    
    @Column(nullable = false)
    private Integer totalAmount;
    
    @Column(nullable = false)
    private Integer paidAmount;
    
    @Column
    private LocalDateTime paidAt;
    
    @Column(length = 500)
    private String deliveryRequest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Team team;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points = new ArrayList<>();

    public int calculateAmount() {
        return orderProducts.stream()
                .mapToInt(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    public int calculateFinalAmount() {
        return calculateAmount() - discountAmount - pointUsedAmount + shippingFee;
    }

    public void updateStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void updatePaymentInfo(String paymentKey, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.paidAt = LocalDateTime.now();
    }
}
