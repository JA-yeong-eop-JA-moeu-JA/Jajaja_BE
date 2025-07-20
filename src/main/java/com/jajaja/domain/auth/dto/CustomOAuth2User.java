package com.jajaja.domain.auth.dto;

import com.jajaja.domain.member.entity.enums.OauthType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record CustomOAuth2User(
        UserDto userDto
) implements OAuth2User {
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return String.valueOf(userDto.userId());
    }

    public String getOAuthId() {
        return userDto.oAuthId();
    }

    public OauthType getOAuthType() {
        return userDto.oAuthType();
    }

    public String getUserName() {
        return userDto.name();
    }
}
