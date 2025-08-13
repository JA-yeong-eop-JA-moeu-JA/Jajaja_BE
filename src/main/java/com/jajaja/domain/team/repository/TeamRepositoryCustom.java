package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeamRepositoryCustom {
    List<Team> findMatchingTeamsByProductId(Long productId);
    Optional<Team> findByIdWithLeaderMembersAndProduct(Long teamId);
    List<Team> findExpiredTeamsWithLeader(TeamStatus status, LocalDateTime now);
    Page<Team> findAllMatchingTeams(Pageable pageable);
}
