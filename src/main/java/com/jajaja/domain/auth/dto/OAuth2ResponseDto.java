package com.jajaja.domain.auth.dto;

import com.jajaja.domain.user.entity.enums.OauthType;

public interface OAuth2ResponseDto {

    OauthType getProvider();

    String getProviderId();

    String getName();

    String getPhone();

    String getEmail();
}
