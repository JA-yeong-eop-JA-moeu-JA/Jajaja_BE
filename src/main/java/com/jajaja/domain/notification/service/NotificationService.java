package com.jajaja.domain.notification.service;

import com.jajaja.domain.notification.dto.request.NotificationCreateRequestDto;
import com.jajaja.domain.notification.dto.response.PagingNotificationResponseDto;
import com.jajaja.domain.notification.dto.response.UnreadCountResponseDto;

public interface NotificationService {
    Long createNotification(NotificationCreateRequestDto requestDto);
    PagingNotificationResponseDto getNotifications(Long memberId, int page, int size);
    void markAsRead(Long notificationId, Long memberId);
    void markAllAsRead(Long memberId);
    UnreadCountResponseDto getUnreadCount(Long memberId);
}