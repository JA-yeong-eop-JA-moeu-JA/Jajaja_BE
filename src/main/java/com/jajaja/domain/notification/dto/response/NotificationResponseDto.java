package com.jajaja.domain.notification.dto.response;

import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.notification.entity.enums.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

public record NotificationResponseDto(
        Long id,
        NotificationType type,
        String title,
        Map<String, Object> detail,
        boolean isRead,
        LocalDateTime createdAt
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static NotificationResponseDto from(Notification notification) {
        Map<String, Object> detailMap = null;
        try {
            if (notification.getDetail() != null) {
                detailMap = objectMapper.readValue(notification.getDetail(), Map.class);
            }
        } catch (JsonProcessingException e) {
            detailMap = Map.of();
        }

        return new NotificationResponseDto(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                detailMap,
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
