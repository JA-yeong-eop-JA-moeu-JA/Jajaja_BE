package com.jajaja.domain.point.repository;

import com.jajaja.domain.order.entity.QOrder;
import com.jajaja.domain.order.entity.QOrderProduct;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.QPoint;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.team.entity.QTeam;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 포인트 기록 페이징 조회
     */
    @Override
    public Page<Point> findByMemberId(Long memberId, Pageable pageable) {
        QPoint point = QPoint.point;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;
        QTeam team = QTeam.team;

        List<Long> pointIds = queryFactory
                .select(point.id)
                .from(point)
                .where(point.member.id.eq(memberId))
                .orderBy(point.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (pointIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Point> points = queryFactory
                .selectFrom(point)
                .distinct()
                .leftJoin(point.orderProduct, orderProduct).fetchJoin()
                .leftJoin(orderProduct.order, order).fetchJoin()
                .leftJoin(orderProduct.product, product).fetchJoin()
                .leftJoin(order.team, team).fetchJoin()
                .where(point.id.in(pointIds))
                .orderBy(point.createdAt.desc())
                .fetch();

        long count = Optional.ofNullable(
                queryFactory.select(point.count()).from(point).where(point.member.id.eq(memberId)).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(points, pageable, count);
    }

    /**
     * 만료된 포인트 찾기 - 스케줄러에서 사용
     */
    @Override
    public List<Point> findExpiringPoints() {
        QPoint point = QPoint.point;
        QPoint subPoint = new QPoint("subPoint");

        return queryFactory
                .selectFrom(point)
                .where(
                        point.type.eq(PointType.REVIEW),
                        point.usedAmount.lt(point.amount),
                        point.expiresAt.lt(LocalDate.now()),
                        point.orderProduct.isNotNull(),
                        JPAExpressions
                                .selectOne()
                                .from(subPoint)
                                .where(
                                        subPoint.type.eq(PointType.EXPIRED),
                                        subPoint.orderProduct.eq(point.orderProduct)
                                )
                                .notExists()
                )
                .fetch();
    }

    /**
     * 유효한 포인트 조회 - 리뷰 포인트 중 사용되지 않은 포인트를 가장 오래된 순서로 조회
     */
    @Override
    public List<Point> findValidReviewPointsOrderedByOldest(Long memberId, LocalDate today) {
        QPoint point = QPoint.point;

        return queryFactory
                .selectFrom(point)
                .where(
                        point.member.id.eq(memberId),
                        point.type.eq(PointType.REVIEW),
                        point.usedAmount.lt(point.amount),
                        point.expiresAt.after(today)
                )
                .orderBy(point.createdAt.asc())
                .fetch();
    }

    /**
     * 리뷰 포인트 조회 - 특정 주문 상품에 대한 "REVIEW 포인트" 조회
     */
    @Override
    public Optional<Point> findReviewPointByOrderProductId(Long orderProductId) {
        QPoint point = QPoint.point;
        return Optional.ofNullable(queryFactory
                .selectFrom(point)
                .where(
                        point.orderProduct.id.eq(orderProductId),
                        point.type.eq(PointType.REVIEW))
                .fetchOne());
    }

    /**
     * 사용된 포인트 조회 - 특정 주문 상품에 대한 "USE 포인트" 조회
     */
    @Override
    public Optional<Point> findUsePointByOrderProductId(Long orderProductId) {
        QPoint point = QPoint.point;
        return Optional.ofNullable(queryFactory
                .selectFrom(point)
                .where(
                        point.orderProduct.id.eq(orderProductId),
                        point.type.eq(PointType.USE)
                ).fetchOne());
    }


    /**
     * 회원의 사용 가능한 포인트들을 생성일 순으로 조회 (FIFO)
     */
    @Override
    public List<Point> findAvailablePointsByMemberIdOrderByCreatedAtAsc(Long memberId) {
        QPoint point = QPoint.point;
        
        return queryFactory
                .selectFrom(point)
                .where(
                        point.member.id.eq(memberId),
                        point.type.in(PointType.REVIEW, PointType.REFUND),
                        point.expiresAt.isNull().or(point.expiresAt.gt(LocalDate.now())),
                        point.amount.subtract(point.usedAmount.coalesce(0)).gt(0)
                )
                .orderBy(point.createdAt.asc())
                .fetch();
    }

    /**
     * 환불용 특정 주문에서 사용된 포인트 조회
     */
    @Override
    public int findUsedPointsByOrderId(Long orderId) {
        QPoint point = QPoint.point;
        
        Integer result = queryFactory
                .select(point.amount.sum())
                .from(point)
                .where(
                        point.type.eq(PointType.USE),
                        point.orderProduct.order.id.eq(orderId)
                )
                .fetchOne();
        
        return result != null ? result : 0;
    }
}
