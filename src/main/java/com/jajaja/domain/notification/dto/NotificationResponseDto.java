package com.jajaja.domain.notification.dto;

import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.notification.entity.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        NotificationType type,
        String body,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getType(),
                notification.getBody(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
