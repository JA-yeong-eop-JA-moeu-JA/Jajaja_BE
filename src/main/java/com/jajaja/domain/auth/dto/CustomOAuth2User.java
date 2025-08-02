package com.jajaja.domain.auth.dto;

import com.jajaja.domain.member.entity.enums.OauthType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record CustomOAuth2User(
        MemberDto memberDto
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
        return String.valueOf(memberDto.memberId());
    }

    public String getOAuthId() {
        return memberDto.oAuthId();
    }

    public OauthType getOAuthType() {
        return memberDto.oAuthType();
    }

    public String getUserName() {
        return memberDto.name();
    }
}
