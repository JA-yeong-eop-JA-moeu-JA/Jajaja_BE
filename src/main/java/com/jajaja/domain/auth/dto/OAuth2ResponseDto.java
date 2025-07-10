package com.jajaja.domain.auth.dto;

public interface OAuth2ResponseDto {

    String getProvider();

    String getProviderId();

    String getName();

    String getPhone();

    String getEmail();
}
