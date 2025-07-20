package com.jajaja.domain.product.entity;

import com.jajaja.domain.member.entity.MemberBusinessCategory;
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
public class BusinessCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "businessCategory")
    private List<MemberBusinessCategory> userBusinessCategories = new ArrayList<>();

    @OneToMany(mappedBy = "businessCategory")
    private List<ProductSales> productSalesList = new ArrayList<>();
}
