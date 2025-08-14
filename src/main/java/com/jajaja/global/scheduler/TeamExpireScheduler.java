package com.jajaja.global.scheduler;


import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.notification.dto.request.NotificationCreateRequestDto;
import com.jajaja.domain.notification.entity.enums.NotificationType;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.order.repository.OrderRepository;
import com.jajaja.domain.order.service.OrderCommandService;
import com.jajaja.domain.order.dto.request.OrderRefundRequestDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamExpireScheduler {

    private final TeamRepository teamRepository;
    private final NotificationService notificationService;
    private final OrderCommandService orderCommandService;

    @Scheduled(fixedDelay = 60000) // 60초마다 실행
    @Transactional
    public void expireTeams() {
        log.info("[스케줄러 실행] TeamExpireScheduler 시작");

        LocalDateTime now = LocalDateTime.now();
        List<Team> expiredTeams = teamRepository.findExpiredTeamsWithAll(TeamStatus.MATCHING, now);

        log.info("만료된 팀 개수 = {}", expiredTeams.size());

        for (Team team : expiredTeams) {
            team.updateStatus(TeamStatus.FAILED);
            log.info("팀 상태 업데이트 → id: {}, status: {}", team.getId(), team.getStatus());

            Member leader = team.getLeader();
            Product product = team.getProduct();
            Order order = team.getOrder();
            Long teamOrderId = (order != null) ? order.getId() : null;

            Map<String, Object> data = new HashMap<>();
            if (teamOrderId != null) data.put("orderId", teamOrderId);
            data.put("productId", product.getId());
            data.put("productName", product.getName());
            data.put("productImage", product.getThumbnailUrl());
            data.put("isTeamMatched", false);

            // 멤버에게 팀 매칭 실패 알림 전송
            notificationService.createNotification(
                    NotificationCreateRequestDto.of(
                            leader.getId(),
                            NotificationType.MATCHING,
                            String.format("‘%s’ 팀 매칭이 실패했습니다.", product.getName()),
                            data
                    )
            );

            // 팀과 연결된 주문을 자동 환불 처리
            if (order != null) {
                try {
                    order.updateStatus(OrderStatus.TEAM_MATCHING_FAILED);
                    OrderRefundRequestDto refundRequest = OrderRefundRequestDto.of(order.getId(), order.getPaymentKey(),"팀 매칭 실패로 인한 자동 환불");
                    orderCommandService.refundOrder(leader.getId(), refundRequest);
                    log.info("팀 매칭 실패 주문 자동 환불 완료 - 주문ID: {}", order.getId());
                } catch (Exception e) {
                    log.error("팀 매칭 실패 주문 자동 환불 중 오류 발생 - 주문ID: {}, 에러: {}", order.getId(), e.getMessage(), e);
                }
            }
        }
    }
}
