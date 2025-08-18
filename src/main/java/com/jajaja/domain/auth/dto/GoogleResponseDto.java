package com.jajaja.domain.auth.dto;

import com.jajaja.domain.member.entity.enums.OauthType;

import java.util.Map;

public record GoogleResponseDto(
        Map<String, Object> attribute
) implements OAuth2ResponseDto {
    @Override
    public OauthType getProvider() {
        return OauthType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }
}
