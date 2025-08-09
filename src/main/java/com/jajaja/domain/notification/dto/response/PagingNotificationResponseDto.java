package com.jajaja.domain.notification.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingNotificationResponseDto(
        PageResponse page,
        List<NotificationResponseDto> notifications
) {
    public static PagingNotificationResponseDto of(Page<?> page, List<NotificationResponseDto> notificationDtos) {
        return PagingNotificationResponseDto.builder()
                .page(PageResponse.from(page))
                .notifications(notificationDtos)
                .build();
    }
}
