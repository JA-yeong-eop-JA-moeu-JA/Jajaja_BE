package com.jajaja.domain.notification.service;

import com.jajaja.domain.notification.dto.NotificationCreateRequestDto;
import com.jajaja.domain.notification.dto.NotificationResponseDto;
import com.jajaja.domain.notification.dto.UnreadCountResponseDto;

import java.util.List;

public interface NotificationService {
    Long createNotification(NotificationCreateRequestDto requestDto);
    List<NotificationResponseDto> getNotifications(Long memberId);
    void markAsRead(Long notificationId, Long memberId);
    void markAllAsRead(Long memberId);
    UnreadCountResponseDto getUnreadCount(Long memberId);
}