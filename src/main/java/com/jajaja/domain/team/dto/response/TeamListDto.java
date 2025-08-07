package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TeamListDto(
        int id,
        String nickname,
        String profileUrl,
        LocalDateTime expireAt
) {

    public static TeamListDto of(Team team, MemberInfoResponseDto memberInfo) {
        return TeamListDto.builder()
                .id(team.getId().intValue())
                .nickname(memberInfo.name())
                .profileUrl(memberInfo.profileUrl())
                .expireAt(team.getExpireAt())
                .build();
    }

}
