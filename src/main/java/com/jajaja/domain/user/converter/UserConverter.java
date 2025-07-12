package com.jajaja.domain.user.converter;

import com.jajaja.domain.auth.dto.OAuth2ResponseDto;
import com.jajaja.domain.user.entity.User;

public class UserConverter {

    public static User toEntity(OAuth2ResponseDto oAuth2Response) {
        return User.builder()
                .oauthId(oAuth2Response.getProviderId())
                .oauthType(oAuth2Response.getProvider())
                .name(oAuth2Response.getName())
                .phone(oAuth2Response.getPhone())
                .email(oAuth2Response.getEmail())
                .build();
    }
}
