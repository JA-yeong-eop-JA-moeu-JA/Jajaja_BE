package com.jajaja.domain.notification.repository;

import com.jajaja.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationRepositoryCustom {
    Page<Notification> findPageByMemberId(Long memberId, int page, int size);
    int markAllAsReadByMemberId(Long memberId);
    int countUnreadByMemberId(Long memberId);
}