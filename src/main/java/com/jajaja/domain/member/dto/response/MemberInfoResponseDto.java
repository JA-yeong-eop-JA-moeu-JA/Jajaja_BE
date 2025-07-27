package com.jajaja.domain.member.dto.response;

import com.jajaja.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponseDto(
        long id,
        String name,
        String profileUrl,
        String phone,
        String email
) {
    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .profileUrl(member.getProfileUrl())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }
}
