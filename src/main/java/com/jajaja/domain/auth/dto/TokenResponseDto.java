package com.jajaja.domain.auth.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
