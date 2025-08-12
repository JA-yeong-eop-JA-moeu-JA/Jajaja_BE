package com.jajaja.domain.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jajaja.domain.notification.entity.enums.NotificationType;

import java.util.Map;

public record NotificationCreateRequestDto(
        Long memberId,
        NotificationType type,
        String title,
        @JsonProperty(defaultValue = "{}")
        Map<String, Object> detail
)  {
    public static NotificationCreateRequestDto of(Long memberId, NotificationType type, String title) {
        return new NotificationCreateRequestDto(memberId, type, title, Map.of());
    }
    public static NotificationCreateRequestDto of(Long memberId, NotificationType type, String title, Map<String, Object> detail) {
        return new NotificationCreateRequestDto(memberId, type, title, detail);
    }
}

