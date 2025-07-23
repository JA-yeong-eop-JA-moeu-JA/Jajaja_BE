package com.jajaja.domain.notification.service;

import java.util.List;
import com.jajaja.domain.notification.dto.NotificationCreateRequestDto;
import com.jajaja.domain.notification.dto.NotificationResponseDto;

public interface NotificationService {
    Long createNotification(NotificationCreateRequestDto requestDto);
    List<NotificationResponseDto> getNotifications(Long memberId);
    void markAsRead(Long notificationId, Long memberId);
    void markAllAsRead(Long memberId);
}