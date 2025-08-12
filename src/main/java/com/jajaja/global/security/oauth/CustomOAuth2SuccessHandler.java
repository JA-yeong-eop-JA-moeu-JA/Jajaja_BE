package com.jajaja.global.security.oauth;

import com.jajaja.domain.redis.entity.RefreshToken;
import com.jajaja.domain.redis.repository.RefreshTokenRepository;
import com.jajaja.global.security.jwt.JwtProperties;
import com.jajaja.global.security.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        RefreshToken refreshTokenEntity = new RefreshToken(authentication.getName(), refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
        jwtProvider.writeTokenCookies(response, accessToken, refreshToken);
        response.sendRedirect(jwtProperties.getRedirectUrl());
    }
}
