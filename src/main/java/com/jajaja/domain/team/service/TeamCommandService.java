package com.jajaja.domain.team.service;

import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;

public interface TeamCommandService {
    TeamCreateResponseDto createTeam(Long memberId, Long productId, String orderId);
    void joinTeam(Long memberId, Long teamId);
    void joinTeamInCarts(Long memberId, Long productId);
}
