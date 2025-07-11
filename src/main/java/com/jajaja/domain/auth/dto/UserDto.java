package com.jajaja.domain.auth.dto;

import com.jajaja.domain.user.entity.enums.OauthType;
import lombok.Builder;

@Builder
public record UserDto(
        long userId,
        OauthType oAuthType,
        String oAuthId,
        String name
) {
    public static UserDto of(long userId, OauthType provider, String providerId, String name) {
        return UserDto.builder()
                .userId(userId)
                .oAuthType(provider)
                .oAuthId(providerId)
                .name(name)
                .build();
    }
}
