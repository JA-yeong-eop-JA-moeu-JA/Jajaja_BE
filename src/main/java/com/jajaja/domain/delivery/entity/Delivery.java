package com.jajaja.domain.delivery.entity;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.user.entity.Member;
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
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 255)
    private String addressDetail;

    @Column(length = 5)
    private String zipcode;

    @Column(length = 255)
    private String doorPassword;

    @Column(nullable = false)
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "delivery")
    private List<Order> orders = new ArrayList<>();
}
