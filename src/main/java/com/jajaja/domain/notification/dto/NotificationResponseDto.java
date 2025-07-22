package com.jajaja.domain.notification.dto;

import com.jajaja.domain.notification.entity.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        NotificationType type,
        String body,
        boolean isRead,
        LocalDateTime createdAt
) {
}
