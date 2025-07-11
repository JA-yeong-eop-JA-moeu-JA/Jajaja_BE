package com.jajaja.domain.team.converter;

import com.jajaja.domain.team.dto.response.TeamResponseDto;
import com.jajaja.domain.team.entity.Team;

public class TeamConverter {

    public static TeamResponseDto toTeamResponseDto(Team team) {
        return TeamResponseDto.of(
                team.getId().intValue(),
                team.getLeader().getName(),
                team.getCreatedAt()
        );
    }
}
