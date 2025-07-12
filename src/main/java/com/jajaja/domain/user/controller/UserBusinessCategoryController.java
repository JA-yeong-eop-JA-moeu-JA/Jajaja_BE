package com.jajaja.domain.user.controller;

import com.jajaja.domain.user.dto.UserBusinessCategoryRequestDto;
import com.jajaja.domain.user.service.UserBusinessCategoryCommandService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class UserBusinessCategoryController {

    private final UserBusinessCategoryCommandService userBusinessCategoryCommandService;


    /**
     * 유저 업종 등록 API
     * @param userId 유저 ID
     * @param dto 업종 등록 요청 DTO
     * @return 200 OK
     */
    @PostMapping("/")
    public ApiResponse<Void> registerUserBusinessCategory(
            @Auth Long userId,
            @RequestBody @Valid UserBusinessCategoryRequestDto dto
    ) {
        userBusinessCategoryCommandService.registerUserBusinessCategory(userId, dto);
        return ApiResponse.onSuccess(null);
    }

}

