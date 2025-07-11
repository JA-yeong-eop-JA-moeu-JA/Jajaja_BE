package com.jajaja.domain.user.controller;

import com.jajaja.domain.user.dto.UserBusinessCategoryRequestDto;
import com.jajaja.domain.user.service.UserBusinessCategoryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class UserBusinessCategoryController {

    private final UserBusinessCategoryService userBusinessCategoryService;

    public UserBusinessCategoryController(UserBusinessCategoryService userBusinessCategoryService) {
        this.userBusinessCategoryService = userBusinessCategoryService;
    }

    /**
     * 유저 업종 등록 API
     * @param userId 유저 ID
     * @param dto 업종 등록 요청 DTO
     * @return 200 OK
     */
    @PostMapping("/")
    public ApiResponse<Void> registerUserBusinessCategory(
            @Parameter(hidden = true) @Auth Long userId,
            @RequestBody @Valid UserBusinessCategoryRequestDto dto
    ) {
        userBusinessCategoryService.registerUserBusinessCategory(userId, dto);
        return ApiResponse.onSuccess(null);
    }

}

