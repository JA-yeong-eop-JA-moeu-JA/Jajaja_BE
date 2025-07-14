package com.jajaja.global.scheduler;


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

    @Scheduled(fixedDelay = 60000) // 60초마다 실행
    @Transactional
    public void expireTeams() {
        log.info("[스케줄러 실행] TeamExpireScheduler 시작");

        LocalDateTime now = LocalDateTime.now();
        List<Team> expiredTeams = teamRepository.findExpiredTeams(TeamStatus.MATCHING, now);

        log.info("만료된 팀 개수 = {}", expiredTeams.size());

        for (Team team : expiredTeams) {
            team.updateStatus(TeamStatus.FAILED);
            log.info("팀 상태 업데이트 → id: {}, status: {}", team.getId(), team.getStatus());
           m
        }
    }
}
