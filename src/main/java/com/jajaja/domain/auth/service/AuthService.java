package com.jajaja.domain.auth.service;

import com.jajaja.domain.auth.dto.TokenResponseDto;
import com.jajaja.global.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;

    public TokenResponseDto getToken(Long userId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(userId), null, null);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        return new TokenResponseDto(accessToken, refreshToken);
    }
}
