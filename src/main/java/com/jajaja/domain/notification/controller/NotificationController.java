package com.jajaja.domain.notification.controller;

import com.jajaja.domain.notification.dto.response.PagingNotificationResponseDto;
import com.jajaja.domain.notification.dto.response.UnreadCountResponseDto;
import com.jajaja.domain.notification.repository.NotificationSseEmitterRepository;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseEmitterRepository emitterRepository;

    @Operation(
            summary = "알림 실시간 구독 API | by 구름/윤윤지",
            description = "사용자가 실시간으로 알림을 받기 위해 SSE 연결을 맺는 API입니다."
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@Auth Long memberId, HttpServletResponse response) {
        return emitterRepository.add(memberId);
    }

    @Operation(
            summary = "알림 목록 조회 API | by 구름/윤윤지",
            description = "사용자의 알림 목록을 페이징 처리하여 조회합니다."
    )
    @GetMapping
    public ApiResponse<PagingNotificationResponseDto> getNotifications(
            @Auth Long memberId,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.onSuccess(notificationService.getNotifications(memberId, page, size));
    }

    @Operation(
            summary = "알림 단건 읽음 API | by 구름/윤윤지",
            description = "사용자의 특정 알림을 읽음 상태로 변경합니다."
    )
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable("notificationId") Long notificationId, @Auth Long memberId) {
        notificationService.markAsRead(notificationId, memberId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "알림 전체 읽음 API | by 구름/윤윤지",
            description = "사용자의 모든 알림을 읽음 상태로 변경합니다."
    )
    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(@Auth Long memberId) {
        notificationService.markAllAsRead(memberId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "읽지 않은 알림 개수 조회 API | by 구름/윤윤지",
            description = "사용자의 읽지 않은 알림 개수를 반환합니다."
    )
    @GetMapping("/unread")
    public ApiResponse<UnreadCountResponseDto> getUnreadCount(@Auth Long memberId) {
        return ApiResponse.onSuccess(notificationService.getUnreadCount(memberId));
    }

}