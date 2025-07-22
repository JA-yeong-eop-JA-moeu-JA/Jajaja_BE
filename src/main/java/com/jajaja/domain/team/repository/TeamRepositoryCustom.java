package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> findMatchingTeamsByProductId(Long productId);
    List<Team> findExpiredTeams(TeamStatus status, LocalDateTime now);
}
