package com.jajaja.domain.notification.repository;

import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notification> findPageByMemberId(Long memberId, int page, int size) {
        QNotification n = QNotification.notification;

        List<Notification> content = queryFactory
                .selectFrom(n)
                .where(n.member.id.eq(memberId))
                .orderBy(n.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(n.count())
                        .from(n)
                        .where(n.member.id.eq(memberId))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }


    @Override
    public int markAllAsReadByMemberId(Long memberId) {
        QNotification n = QNotification.notification;
        return (int) queryFactory.update(n)
                .set(n.isRead, true)
                .where(n.member.id.eq(memberId).and(n.isRead.isFalse()))
                .execute();
    }

    @Override
    public int countUnreadByMemberId(Long memberId) {
        QNotification n = QNotification.notification;
        return (int) queryFactory
                .selectFrom(n)
                .where(n.member.id.eq(memberId).and(n.isRead.isFalse()))
                .fetchCount();
    }

}