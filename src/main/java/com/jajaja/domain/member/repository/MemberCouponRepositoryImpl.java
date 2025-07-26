package com.jajaja.domain.member.repository;

import com.jajaja.domain.coupon.entity.enums.CouponStatus;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.domain.member.entity.QMemberCoupon;
import com.jajaja.domain.coupon.entity.QCoupon;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberCoupon> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable) {
        QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
        QCoupon coupon = QCoupon.coupon;

        // 1단계: 페이징된 MemberCoupon ID들만 조회
        List<Long> memberCouponIds = queryFactory
                .select(memberCoupon.id)
                .from(memberCoupon)
                .where(memberCoupon.member.id.eq(memberId)
                        .and(memberCoupon.status.eq(CouponStatus.AVAILABLE)))
                .orderBy(memberCoupon.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 빈 결과 처리
        if (memberCouponIds.isEmpty()) {
            return PageableExecutionUtils.getPage(List.of(), pageable, () -> 0L);
        }

        // 2단계: 조회된 ID들로 fetchJoin을 통해 엔티티 조회
        List<MemberCoupon> content = queryFactory
                .selectFrom(memberCoupon)
                .join(memberCoupon.coupon, coupon).fetchJoin()
                .where(memberCoupon.id.in(memberCouponIds))
                .orderBy(memberCoupon.createdAt.desc())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(memberCoupon.count())
                .from(memberCoupon)
                .where(memberCoupon.member.id.eq(memberId)
                        .and(memberCoupon.status.eq(CouponStatus.AVAILABLE)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}