package com.jajaja.domain.team.service;

import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;

public interface TeamCommandService {
    TeamCreateResponseDto createTeam(Long userId, Long productId);
    void joinTeam(Long userId, Long teamId);
    void joinTeamInCarts(Long userId, Long productId);
}
