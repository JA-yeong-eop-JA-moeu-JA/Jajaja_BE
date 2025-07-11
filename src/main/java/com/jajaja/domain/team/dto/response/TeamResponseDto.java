package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TeamResponseDto(
        int id,
        String nickname,
        LocalDateTime createdAt
) {
    public static TeamResponseDto of(int id, String nickname, LocalDateTime createdAt) {
        return TeamResponseDto.builder()
                .id(id)
                .nickname(nickname)
                .createdAt(createdAt)
                .build();
    }

    public static TeamResponseDto of(Team team) {
        return TeamResponseDto.builder()
                .id(team.getId().intValue())
                .nickname(team.getLeader().getName())
                .createdAt(team.getCreatedAt())
                .build();
    }
}
