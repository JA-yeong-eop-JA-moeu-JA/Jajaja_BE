package com.jajaja.domain.auth.controller;

import com.jajaja.domain.auth.dto.TokenResponseDto;
import com.jajaja.domain.auth.service.AuthService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "임시 토큰 발급 API | by 지안/윤진수",
            description = "사용자 ID를 기반으로 액세스 토큰과 리프레쉬 토큰을 발급합니다."
    )
    @PostMapping("/token")
    public ApiResponse<TokenResponseDto> getToken(@RequestParam Long memberId) {
        return ApiResponse.onSuccess(authService.getToken(memberId));
    }

    @Operation(
            summary = "토큰 재발급 API | by 지안/윤진수",
            description = "리프레쉬 토큰을 사용하여 새로운 액세스 토큰과 리프레쉬 토큰을 쿠키로 발급합니다."
    )
    @PostMapping("/reissue")
    public ApiResponse<?> reissueToken(
            @Parameter(hidden = true)
            @CookieValue(required = false) String refreshToken, HttpServletResponse response) {
        authService.reissueToken(refreshToken, response);
        return ApiResponse.onSuccess(null);
    }
}
