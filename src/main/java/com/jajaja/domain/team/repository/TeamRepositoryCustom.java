package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.Team;

import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> findMatchingTeamsByProductId(Long productId);
}
