package com.jajaja.domain.member.repository;

import com.jajaja.domain.member.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponRepositoryCustom {
    Page<MemberCoupon> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
}