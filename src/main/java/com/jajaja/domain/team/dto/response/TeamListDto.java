package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TeamListDto(
        int id,
        String nickname,
        LocalDateTime expireAt
) {

    public static TeamListDto from(Team team) {
        return TeamListDto.builder()
                .id(team.getId().intValue())
                .nickname(team.getLeader().getName())
                .expireAt(team.getExpireAt())
                .build();
    }

}
