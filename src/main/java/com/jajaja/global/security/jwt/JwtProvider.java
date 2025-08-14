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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

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
        return generateToken(authentication, jwtProperties.getExpiration().getAccess(), TokenType.ACCESS);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.getExpiration().getRefresh(), TokenType.REFRESH);
    }

    public String generateConsentToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.getExpiration().getConsent(), TokenType.CONSENT);
    }

    public String generateToken(Authentication authentication, long expirationTime, TokenType tokenType) {
        String memberId = authentication.getName();
        return Jwts.builder()
                .claim("memberId", Long.parseLong(memberId))
                .claim("purpose", tokenType.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String validateAccessToken(String token) {
        try {
            Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
            return claims.get("purpose", String.class);
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

    public Authentication getAuthentication(String token, List<GrantedAuthority> authorities) {
        Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
        Long memberId = claims.get("memberId", Long.class);
        MemberDto memberDto = MemberDto.builder().memberId(memberId).build();
        CustomOAuth2User principal = new CustomOAuth2User(memberDto);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public void writeTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(StringUtils.hasText(accessToken) ? jwtProperties.getExpiration().getAccess() / 1000 : 0);

        ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(StringUtils.hasText(refreshToken) ? jwtProperties.getExpiration().getRefresh() / 1000 : 0);


        if (StringUtils.hasText(jwtProperties.getCookieDomain())) {
            accessBuilder.domain(jwtProperties.getCookieDomain());
            refreshBuilder.domain(jwtProperties.getCookieDomain());
        }

        response.addHeader("Set-Cookie", accessBuilder.build().toString());
        response.addHeader("Set-Cookie", refreshBuilder.build().toString());
    }

    public void writeConsentTokenCookie(HttpServletResponse response, String consentToken) {
        writeTokenCookies(response, "", "");
        String consentPath = jwtProperties.getConsentPath();
        ResponseCookie.ResponseCookieBuilder consentBuilder = ResponseCookie.from("accessToken", consentToken)
                .path(consentPath)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(jwtProperties.getExpiration().getConsent() / 1000);

        if (StringUtils.hasText(jwtProperties.getCookieDomain())) {
            consentBuilder.domain(jwtProperties.getCookieDomain());
        }

        response.addHeader("Set-Cookie", consentBuilder.build().toString());
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
    }
}