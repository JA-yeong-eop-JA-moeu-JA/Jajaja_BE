package com.jajaja.domain.notification.dto;

import com.jajaja.domain.notification.entity.enums.NotificationType;

public record NotificationCreateRequestDto(
        Long memberId,
        NotificationType type,
        String body
) {
}
