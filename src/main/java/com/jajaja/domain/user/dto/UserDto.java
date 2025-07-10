package com.jajaja.domain.user.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String oAuthType,
        String oAuthId,
        String name
) {
    public static UserDto of(String provider, String providerId, String name) {
        return UserDto.builder()
                .oAuthType(provider)
                .oAuthId(providerId)
                .name(name)
                .build();
    }
}
