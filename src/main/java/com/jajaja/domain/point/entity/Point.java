package com.jajaja.domain.point.entity;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.jajaja.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointType type;

    @Column(nullable = false)
    private Integer amount;

    @Column
    private Integer usedAmount;

    @Column
    private LocalDate expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    public int getAvailableAmount() {
        return amount - (usedAmount != null ? usedAmount : 0);
    }

    public void use(int amountToUse) {
        if (getAvailableAmount() < amountToUse) {
            throw new BadRequestException(ErrorStatus.INSUFFICIENT_POINT);
        }
        this.usedAmount += amountToUse;
    }

    public void decreaseUsedAmount(int amountToDecrease) {
        if (this.usedAmount == null) {
            this.usedAmount = 0;
        }
        if (this.usedAmount < amountToDecrease) {
            throw new BadRequestException(ErrorStatus.INVALID_POINT_OPERATION);
        }
        this.usedAmount -= amountToDecrease;
    }
}