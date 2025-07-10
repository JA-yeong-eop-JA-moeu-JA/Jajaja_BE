package com.jajaja.domain.auth.dto;

import com.jajaja.domain.user.entity.enums.OauthType;
import lombok.Builder;

@Builder
public record UserDto(
        OauthType oAuthType,
        String oAuthId,
        String name
) {
    public static UserDto of(OauthType provider, String providerId, String name) {
        return UserDto.builder()
                .oAuthType(provider)
                .oAuthId(providerId)
                .name(name)
                .build();
    }
}
