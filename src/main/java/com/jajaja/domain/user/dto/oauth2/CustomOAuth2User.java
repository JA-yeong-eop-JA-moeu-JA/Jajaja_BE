package com.jajaja.domain.user.dto.oauth2;

import com.jajaja.domain.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;;
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
        return userDto.name();
    }

    public String getOAuthId() {
        return userDto.oAuthId();
    }

    public String getOAuthType() {
        return userDto.oAuthType();
    }
}
