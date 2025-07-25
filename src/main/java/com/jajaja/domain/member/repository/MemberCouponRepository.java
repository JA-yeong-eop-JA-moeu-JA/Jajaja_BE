package com.jajaja.domain.member.repository;

import com.jajaja.domain.member.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("SELECT mc FROM MemberCoupon mc JOIN FETCH mc.coupon c " +
           "WHERE mc.member.id = :memberId " +
           "ORDER BY mc.createdAt DESC")
    Page<MemberCoupon> findByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId, Pageable pageable);
}