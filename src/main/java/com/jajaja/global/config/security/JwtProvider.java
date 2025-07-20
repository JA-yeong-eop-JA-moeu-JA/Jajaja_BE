package com.jajaja.global.config.security;

import com.jajaja.domain.auth.dto.CustomOAuth2User;
import com.jajaja.domain.auth.dto.UserDto;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.getExpiration().getAccess());
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.getExpiration().getRefresh());
    }

    public String generateToken(Authentication authentication, long expirationTime) {
        String userId = authentication.getName();
        return Jwts.builder()
                .claim("userId", Long.parseLong(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public void validateAccessToken(String token) {
        try {
            getJwtParser().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorStatus.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorStatus.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateRefreshToken(String token) {
        try {
            getJwtParser().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
        Long userId = claims.get("userId", Long.class);
        UserDto userDto = UserDto.builder().userId(userId).build();
        CustomOAuth2User principal = new CustomOAuth2User(userDto);
        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    public void writeTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                .maxAge(jwtProperties.getExpiration().getAccess())
                .domain(jwtProperties.getCookieDomain())
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .maxAge(jwtProperties.getExpiration().getRefresh())
                .domain(jwtProperties.getCookieDomain())
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
    }
}