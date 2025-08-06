package com.jajaja.domain.notification.controller;

import java.util.List;

import com.jajaja.domain.notification.repository.NotificationSseEmitterRepository;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.domain.notification.dto.NotificationResponseDto;
import com.jajaja.domain.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseEmitterRepository emitterRepository;

    @Operation(
            summary = "알림 실시간 구독 API | by 구름/윤윤지",
            description = "사용자가 실시간으로 알림을 받기 위해 SSE 연결을 맺는 API입니다."
    )
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@Auth Long memberId) {
        return emitterRepository.add(memberId);
    }

    @Operation(
            summary = "알림 목록 조회 API | by 구름/윤윤지",
            description = "사용자의 알림 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<List<NotificationResponseDto>> getNotifications(@Auth Long memberId) {
        return ApiResponse.onSuccess(notificationService.getNotifications(memberId));
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
}