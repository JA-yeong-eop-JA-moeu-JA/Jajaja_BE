package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeamRepositoryCustom {
    List<Team> findMatchingTeamsByProductId(Long productId);
    Optional<Team> findByIdWithLeaderAndMembers(Long teamId);
    List<Team> findExpiredTeamsWithLeader(TeamStatus status, LocalDateTime now);
}
