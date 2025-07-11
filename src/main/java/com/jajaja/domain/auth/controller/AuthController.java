package com.jajaja.domain.auth.controller;

import com.jajaja.domain.auth.dto.TokenResponseDto;
import com.jajaja.domain.auth.service.AuthService;
import com.jajaja.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ApiResponse<TokenResponseDto> getToken(@RequestParam Long userId) {
        return ApiResponse.onSuccess(authService.getToken(userId));
    }
}
