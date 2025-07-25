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
}
