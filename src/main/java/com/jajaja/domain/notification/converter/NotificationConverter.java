package com.jajaja.domain.notification.converter;

import org.springframework.stereotype.Component;
import com.jajaja.domain.notification.dto.NotificationResponseDto;
import com.jajaja.domain.notification.entity.Notification;

@Component
public class NotificationConverter {

    public NotificationResponseDto toResponseDto(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getType(),
                notification.getBody(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
