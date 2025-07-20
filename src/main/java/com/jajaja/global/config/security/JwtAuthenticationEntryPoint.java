package com.jajaja.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                ErrorStatus.UNAUTHORIZED.getCode(),
                ErrorStatus.UNAUTHORIZED.getMessage(),
                null
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
