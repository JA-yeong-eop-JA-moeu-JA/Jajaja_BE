package com.jajaja.domain.auth.service;

import com.jajaja.domain.user.converter.UserConverter;
import com.jajaja.domain.auth.dto.UserDto;
import com.jajaja.domain.auth.dto.CustomOAuth2User;
import com.jajaja.domain.auth.dto.GoogleResponseDto;
import com.jajaja.domain.auth.dto.KakaoResponseDto;
import com.jajaja.domain.auth.dto.OAuth2ResponseDto;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.entity.enums.OauthType;
import com.jajaja.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
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
        String oauthId = oAuth2Response.getProviderId();
        OauthType oauthType = oAuth2Response.getProvider();
        User existingUser = userRepository.findByOauthIdAndOauthType(oauthId, oauthType);
        User user = null;
        if (existingUser == null) {
            user = UserConverter.toEntity(oAuth2Response);
            userRepository.save(user);
        } else {
            existingUser.updateName(oAuth2Response.getName());
            existingUser.updatePhone(oAuth2Response.getPhone());
            existingUser.updateEmail(oAuth2Response.getEmail());
            user = existingUser;
        }
        UserDto userDto = UserDto.of(user.getId(), oauthType, oauthId, oAuth2Response.getName());
        return new CustomOAuth2User(userDto);
    }
}
