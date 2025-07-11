package com.jajaja.domain.team.dto.response;

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
}
