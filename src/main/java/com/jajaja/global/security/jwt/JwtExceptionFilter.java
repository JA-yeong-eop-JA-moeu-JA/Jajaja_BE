package com.jajaja.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            setErrorResponse(response, e.getErrorStatus());
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, ErrorStatus.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            setErrorResponse(response, ErrorStatus.INVALID_ACCESS_TOKEN);
        } catch (Exception e) {
            setErrorResponse(response, ErrorStatus._INTERNAL_SERVER_ERROR);
        }

    }

    private void setErrorResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
