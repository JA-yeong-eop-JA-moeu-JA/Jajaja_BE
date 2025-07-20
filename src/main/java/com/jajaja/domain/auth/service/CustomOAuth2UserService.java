package com.jajaja.domain.auth.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.repository.CartRepository;
import com.jajaja.domain.member.converter.MemberConverter;
import com.jajaja.domain.auth.dto.UserDto;
import com.jajaja.domain.auth.dto.CustomOAuth2User;
import com.jajaja.domain.auth.dto.GoogleResponseDto;
import com.jajaja.domain.auth.dto.KakaoResponseDto;
import com.jajaja.domain.auth.dto.OAuth2ResponseDto;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.enums.OauthType;
import com.jajaja.domain.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

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
        Member existingMember = memberRepository.findByOauthIdAndOauthType(oauthId, oauthType);
        Member member = null;
        if (existingMember == null) {
            member = MemberConverter.toEntity(oAuth2Response);
            memberRepository.save(member);
            // 장바구니 생성
            cartRepository.save(Cart.builder().member(member).build());
        } else {
            existingMember.updateName(oAuth2Response.getName());
            existingMember.updatePhone(oAuth2Response.getPhone());
            existingMember.updateEmail(oAuth2Response.getEmail());
            member = existingMember;
        }
        UserDto userDto = UserDto.of(member.getId(), oauthType, oauthId, oAuth2Response.getName());
        return new CustomOAuth2User(userDto);
    }
}
