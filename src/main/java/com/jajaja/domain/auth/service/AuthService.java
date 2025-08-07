package com.jajaja.domain.auth.service;

import com.jajaja.domain.auth.dto.TokenResponseDto;
import com.jajaja.domain.redis.entity.RefreshToken;
import com.jajaja.domain.redis.repository.RefreshTokenRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.UnauthorizedException;
import com.jajaja.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponseDto getToken(Long memberId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(memberId), null, null);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public void reissueToken(String refreshToken, HttpServletResponse response) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new UnauthorizedException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND);
        }
        jwtProvider.validateRefreshToken(refreshToken);
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        // Redis에 저장된 refreshToken과 비교하여 일치하는지 확인
        String memberId = authentication.getName();
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException(ErrorStatus.NOT_MATCH_REFRESH_TOKEN));
        if (!refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException(ErrorStatus.NOT_MATCH_REFRESH_TOKEN);
        }

        // 새로 accessToken과 refreshToken 발급
        String newAccessToken = jwtProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtProvider.generateRefreshToken(authentication);

        // Redis에 새로 발급한 refreshToken 저장
        RefreshToken newRefreshTokenEntity = new RefreshToken(memberId, newRefreshToken);
        refreshTokenRepository.save(newRefreshTokenEntity);

        jwtProvider.writeTokenCookies(response, newAccessToken, newRefreshToken);
    }

    public void logout(Long memberId, HttpServletResponse response) {
        refreshTokenRepository.deleteById(memberId.toString());
        String accessToken = "";
        String refreshToken = "";
        jwtProvider.writeTokenCookies(response, accessToken, refreshToken);
    }
}
