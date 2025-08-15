package com.jajaja.domain.auth.dto;

import com.jajaja.domain.member.entity.enums.OauthType;

import java.util.Map;

public record KakaoResponseDto(
        Map<String, Object> attribute
) implements OAuth2ResponseDto {
    private Map<String, Object> getKakaoAccount() {
        return (Map<String, Object>) attribute.get("kakao_account");
    }

    @Override
    public OauthType getProvider() {
        return OauthType.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoProfile = (Map<String, Object>) getKakaoAccount().get("profile");
        return kakaoProfile.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return getKakaoAccount().get("email").toString();
    }
}
