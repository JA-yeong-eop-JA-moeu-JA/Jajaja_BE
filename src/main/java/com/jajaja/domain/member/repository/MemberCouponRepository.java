package com.jajaja.domain.member.repository;

import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, MemberCouponRepositoryCustom {
    Optional<MemberCoupon> findByMemberAndCoupon(Member member, Coupon coupon);
    Optional<MemberCoupon> findByMemberIdAndCouponIdAndUsedAtIsNull(Long memberId, Long couponId);
}