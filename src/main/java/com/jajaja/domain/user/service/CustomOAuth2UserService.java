package com.jajaja.domain.user.service;

import com.jajaja.domain.user.dto.UserDto;
import com.jajaja.domain.user.dto.oauth2.CustomOAuth2User;
import com.jajaja.domain.user.dto.oauth2.GoogleResponseDto;
import com.jajaja.domain.user.dto.oauth2.KakaoResponseDto;
import com.jajaja.domain.user.dto.oauth2.OAuth2ResponseDto;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ResponseDto oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponseDto(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponseDto(oAuth2User.getAttributes());
        } else {
            return null;
        }
        // TODO: 사용자 저장 로직 추가
        UserDto userDto = UserDto.of(oAuth2Response.getProvider(), oAuth2Response.getProviderId(), oAuth2Response.getName());
        return new CustomOAuth2User(userDto);
    }
}
