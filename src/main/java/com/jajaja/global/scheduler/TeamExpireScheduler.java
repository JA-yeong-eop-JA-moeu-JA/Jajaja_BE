package com.jajaja.global.scheduler;


import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.notification.dto.NotificationCreateRequestDto;
import com.jajaja.domain.notification.entity.enums.NotificationType;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamExpireScheduler {

    private final TeamRepository teamRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedDelay = 60000) // 60초마다 실행
    @Transactional
    public void expireTeams() {
        log.info("[스케줄러 실행] TeamExpireScheduler 시작");

        LocalDateTime now = LocalDateTime.now();
        List<Team> expiredTeams = teamRepository.findExpiredTeamsWithLeader(TeamStatus.MATCHING, now);

        log.info("만료된 팀 개수 = {}", expiredTeams.size());

        for (Team team : expiredTeams) {
            team.updateStatus(TeamStatus.FAILED);
            log.info("팀 상태 업데이트 → id: {}, status: {}", team.getId(), team.getStatus());

            Member leader = team.getLeader();

            // 멤버에게 팀 매칭 실패 알림 전송
            notificationService.createNotification(new NotificationCreateRequestDto(leader.getId(), NotificationType.MATCHING, "팀 매칭에 실패했습니다."));
        }
    }
}
