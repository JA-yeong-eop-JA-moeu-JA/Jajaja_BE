package com.jajaja.domain.order.repository;

import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.QOrder;
import com.jajaja.domain.order.entity.QOrderProduct;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.product.entity.QProductOption;
import com.jajaja.domain.team.entity.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findByMemberId(Long memberId, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QProduct product = QProduct.product;
        QProductOption productOption = QProductOption.productOption;
        QTeam team = QTeam.team;

        List<Long> orderIds = queryFactory
                .select(order.id)
                .from(order)
                .where(order.member.id.eq(memberId))
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (orderIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Order> orders = queryFactory
                .selectFrom(order)
                .distinct()
                .leftJoin(order.orderProducts, orderProduct).fetchJoin()
                .leftJoin(orderProduct.product, product).fetchJoin()
                .leftJoin(orderProduct.productOption, productOption).fetchJoin()
                .leftJoin(order.team, team).fetchJoin()
                .where(order.id.in(orderIds))
                .orderBy(order.createdAt.desc())
                .fetch();

        long count = Optional.ofNullable(
                queryFactory.select(order.count()).from(order).where(order.member.id.eq(memberId)).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(orders, pageable, count);
    }
}
