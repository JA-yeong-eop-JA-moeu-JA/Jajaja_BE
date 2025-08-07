package com.jajaja.domain.notification.repository;

import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findNotificationsByMemberId(Long memberId, int offset, int limit) {
        QNotification n = QNotification.notification;
        return queryFactory
                .selectFrom(n)
                .where(n.member.id.eq(memberId))
                .orderBy(n.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
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