package com.jajaja.domain.user.dto.oauth2;

import java.util.Map;

public record GoogleResponseDto(
        Map<String, Object> attribute
) implements OAuth2ResponseDto {
    @Override
    public String getProvider() {
        return "google";
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

    @Override
    public String getPhone() {
        return attribute.get("phone_number").toString();
    }
}
