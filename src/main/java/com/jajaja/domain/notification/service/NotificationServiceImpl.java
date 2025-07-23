package com.jajaja.domain.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import com.jajaja.domain.notification.repository.NotificationSseEmitterRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.notification.dto.NotificationCreateRequestDto;
import com.jajaja.domain.notification.dto.NotificationResponseDto;
import com.jajaja.domain.notification.entity.Notification;
import com.jajaja.domain.notification.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final NotificationSseEmitterRepository emitterRepository;

    @Override
    @Transactional
    public Long createNotification(NotificationCreateRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.memberId())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));

        Notification notification = Notification.builder()
                .member(member)
                .type(requestDto.type())
                .body(requestDto.body())
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        emitterRepository.send(member.getId(), NotificationResponseDto.from(saved));

        return saved.getId();
    }

    @Override
    public List<NotificationResponseDto> getNotifications(Long memberId) {
        return notificationRepository.findNotificationsByMemberId(memberId).stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAllAsRead(Long memberId) {
        notificationRepository.markAllAsReadByMemberId(memberId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.NOTIFICATION_NOT_FOUND));
        validateOwnership(notification, memberId);
        notification.markAsRead();
    }

    private void validateOwnership(Notification notification, Long memberId) {
        if (!notification.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException(ErrorStatus.NOTIFICATION_ACCESS_DENIED.getMessage());
        }
    }
}