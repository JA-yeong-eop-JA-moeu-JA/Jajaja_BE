package com.jajaja.domain.auth.dto;

import com.jajaja.domain.member.entity.enums.OauthType;
import lombok.Builder;

@Builder
public record MemberDto(
        long memberId,
        OauthType oAuthType,
        String oAuthId,
        String name
) {
    public static MemberDto of(long memberId, OauthType provider, String providerId, String name) {
        return MemberDto.builder()
                .memberId(memberId)
                .oAuthType(provider)
                .oAuthId(providerId)
                .name(name)
                .build();
    }
}
