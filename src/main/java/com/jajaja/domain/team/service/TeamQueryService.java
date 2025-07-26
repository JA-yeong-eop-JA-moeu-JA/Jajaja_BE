package com.jajaja.domain.team.service;

import com.jajaja.domain.team.dto.response.TeamProductListResponseDto;

public interface TeamQueryService {
    TeamProductListResponseDto getMatchingTeamProducts(int page, int size);
}