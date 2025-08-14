package com.jajaja.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN = "accessToken";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            String purpose = jwtProvider.validateAccessToken(token);

            List<GrantedAuthority> authorities = switch (purpose) {
                case "ACCESS" -> List.of(new SimpleGrantedAuthority("ROLE_ACCESS"));
                case "CONSENT" -> List.of(new SimpleGrantedAuthority("ROLE_CONSENT"));
                default -> List.of();
            };

            if (!authorities.isEmpty()) {
                Authentication authentication = jwtProvider.getAuthentication(token, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 가져오기
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }

        // 쿠키에서 토큰 가져오기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(ACCESS_TOKEN)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
