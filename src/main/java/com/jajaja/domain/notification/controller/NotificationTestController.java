package com.jajaja.domain.notification.controller;

import com.jajaja.domain.notification.dto.request.NotificationCreateRequestDto;
import com.jajaja.domain.notification.repository.NotificationSseEmitterRepository;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications/test")
@RequiredArgsConstructor
public class NotificationTestController {

    private final NotificationService notificationService;
    private final NotificationSseEmitterRepository emitterRepository;

    @PostMapping
    public ApiResponse<Long> createTestNotification(@RequestBody NotificationCreateRequestDto dto) {
        Long id = notificationService.createNotification(dto);
        return ApiResponse.onSuccess(id);
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter testSubscribe() {
        Long testMemberId = 1L; // 테스트용 고정 ID
        return emitterRepository.add(testMemberId);
    }
}
