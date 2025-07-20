package com.jajaja.domain.team.entity;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.user.entity.Member;
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
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TeamStatus status = TeamStatus.MATCHING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
  
    /**
     * Team의 status를 업데이트
     * @paramname status TeamStatus
     */
    public void updateStatus(TeamStatus status) {
        this.status = status;
    }
}
