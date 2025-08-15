package com.jajaja.global.security;

import com.jajaja.domain.auth.service.CustomOAuth2UserService;
import com.jajaja.global.config.CorsConfig;
import com.jajaja.global.security.jwt.JwtAuthenticationEntryPoint;
import com.jajaja.global.security.jwt.JwtAuthenticationFilter;
import com.jajaja.global.security.jwt.JwtExceptionFilter;
import com.jajaja.global.security.jwt.JwtProvider;
import com.jajaja.global.security.oauth.CustomOAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String[] WHITELIST = {
            "/", "/swagger/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**", "/health"
    };

    private static final String[] GET_WHITELIST = {
            "/api/products/**", "/api/search/**", "/api/categories/**", "/api/reviews/**", "/api/search/recent-keywords", "/api/teams/products"
    };

    private static final String[] POST_WHITELIST = {
            // 인증 필요 없이 허용할 POST 요청 URI가 있다면 여기에 추가
    };

    private static final String[] PATCH_WHITELIST = {
            // 인증 필요 없이 허용할 PATCH 요청 URI가 있다면 여기에 추가
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler))
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.POST, POST_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.PATCH, PATCH_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .build();
    }
}
