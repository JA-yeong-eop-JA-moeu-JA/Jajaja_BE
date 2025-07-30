package com.jajaja.domain.member.entity;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewLike;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.TeamMember;
import com.jajaja.domain.member.entity.enums.OauthType;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OauthType oauthType;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(name = "profile_url", length = 512)
    private String profileUrl;

    @Column(length = 16)
    private String phone;

    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private Integer point;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private MemberBusinessCategory memberBusinessCategory;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> memberCoupons = new ArrayList <>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teamsAsLeader = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
  
    public void updatePoint(int point) { this.point = point; }
    
    public void updateProfileUrl(String profileUrl) { this.profileUrl = profileUrl; }
}