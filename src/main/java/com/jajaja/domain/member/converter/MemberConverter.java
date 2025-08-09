package com.jajaja.domain.member.converter;

import com.jajaja.domain.auth.dto.OAuth2ResponseDto;
import com.jajaja.domain.member.entity.Member;

public class MemberConverter {

    public static Member toEntity(OAuth2ResponseDto oAuth2Response) {
        return Member.builder()
                .oauthId(oAuth2Response.getProviderId())
                .oauthType(oAuth2Response.getProvider())
                .name(oAuth2Response.getName())
                .phone(oAuth2Response.getPhone())
                .email(oAuth2Response.getEmail())
                .profileKeyName("default-profile-image.png")
                .point(0)
                .build();
    }
}
