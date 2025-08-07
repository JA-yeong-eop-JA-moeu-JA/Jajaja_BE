package com.jajaja.global.scheduler;

import com.jajaja.domain.notification.dto.request.NotificationCreateRequestDto;
import com.jajaja.domain.notification.entity.enums.NotificationType;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointExpireScheduler {

    private final PointRepository pointRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void expirePoints() {
        log.info("[스케줄러 실행] PointExpireScheduler 시작");

        List<Point> expiredPoints = pointRepository.findExpiringPoints();

        log.info("만료된 포인트 수 = {}", expiredPoints.size());

        for (Point point : expiredPoints) {
            Point expiredPoint = Point.builder()
                    .type(PointType.EXPIRED)
                    // 남은 포인트만큼 만료(차감) 처리
                    .amount(point.getAvailableAmount())
                    .member(point.getMember())
                    .orderProduct(point.getOrderProduct())
                    .build();
            pointRepository.save(expiredPoint);
            // 사용자에게 포인트 만료 알림 전송
            notificationService.createNotification(new NotificationCreateRequestDto(point.getMember().getId(), NotificationType.POINT_EXPIRED, "포인트가 만료되었습니다."));
        }
    }
}
