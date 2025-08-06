package com.jajaja.global.security.jwt;

import com.jajaja.domain.auth.dto.CustomOAuth2User;
import com.jajaja.domain.auth.dto.MemberDto;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.token.same-site}")
    private String sameSite;

    @Value("${jwt.token.secure}")
    private Boolean secure;

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
        String memberId = authentication.getName();
        return Jwts.builder()
                .claim("memberId", Long.parseLong(memberId))
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
        Long memberId = claims.get("memberId", Long.class);
        MemberDto memberDto = MemberDto.builder().memberId(memberId).build();
        CustomOAuth2User principal = new CustomOAuth2User(memberDto);
        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    public void writeTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(jwtProperties.getExpiration().getAccess() / 1000);

        ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(jwtProperties.getExpiration().getRefresh() / 1000);


        if (jwtProperties.getCookieDomain() != null && !jwtProperties.getCookieDomain().isBlank()) {
            accessBuilder.domain(jwtProperties.getCookieDomain()).build();
            refreshBuilder.domain(jwtProperties.getCookieDomain()).build();
        }

        response.addHeader("Set-Cookie", accessBuilder.build().toString());
        response.addHeader("Set-Cookie", refreshBuilder.build().toString());
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
    }
}