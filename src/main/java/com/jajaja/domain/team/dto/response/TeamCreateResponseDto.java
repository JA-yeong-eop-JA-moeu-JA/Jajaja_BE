package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

@Builder
public record TeamCreateResponseDto(int teamId) {

    public static TeamCreateResponseDto from(Team team) {
        return TeamCreateResponseDto.builder()
                .teamId(team.getId().intValue())
                .build();
    }
}
