package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamCommandRepository extends JpaRepository<Team, Long> {
	boolean existsByProductIdAndStatus(Long productId, TeamStatus status);
}
