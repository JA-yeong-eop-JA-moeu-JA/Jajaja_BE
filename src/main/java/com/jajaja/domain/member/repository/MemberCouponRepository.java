package com.jajaja.domain.member.repository;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("SELECT mc FROM MemberCoupon mc " +
            "JOIN FETCH mc.coupon c " +
           "WHERE mc.member.id = :memberId " +
           "AND mc.status = com.jajaja.domain.coupon.entity.enums.CouponStatus.AVAILABLE " +
           "ORDER BY mc.createdAt DESC")
    Page<MemberCoupon> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    Optional<MemberCoupon> findByMemberAndCoupon(Member member, Coupon coupon);
}