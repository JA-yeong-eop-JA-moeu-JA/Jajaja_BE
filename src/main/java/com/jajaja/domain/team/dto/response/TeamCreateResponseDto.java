package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record TeamCreateResponseDto(int teamId, String createdAt) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static TeamCreateResponseDto from(Team team) {
        return TeamCreateResponseDto.builder()
                .teamId(team.getId().intValue())
                .createdAt(team.getCreatedAt().format(FORMATTER))
                .build();
    }
}
