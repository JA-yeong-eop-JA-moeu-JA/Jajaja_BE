package com.jajaja.domain.notification.repository;

import com.jajaja.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findNotificationsByMemberId(Long memberId);
    int markAllAsReadByMemberId(Long memberId);
    int countUnreadByMemberId(Long memberId);
}