package com.jajaja.domain.member.controller;

import com.jajaja.domain.member.dto.MemberBusinessCategoryRequestDto;
import com.jajaja.domain.member.service.MemberBusinessCategoryCommandService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class MemberBusinessCategoryController {

    private final MemberBusinessCategoryCommandService memberBusinessCategoryCommandService;


    /**
     * 유저 업종 등록 API
     * @param userId 유저 ID
     * @param dto 업종 등록 요청 DTO
     * @return 200 OK
     */
    @Operation(
            summary = "유저 업종 등록 API | by 구름/윤윤지",
            description = "온보딩 단계에서 유저가 선택한 업종을 등록합니다."
    )
    @PostMapping("/")
    public ApiResponse<Void> registerUserBusinessCategory(
            @Auth Long userId,
            @RequestBody @Valid MemberBusinessCategoryRequestDto dto
    ) {
        memberBusinessCategoryCommandService.registerUserBusinessCategory(userId, dto);
        return ApiResponse.onSuccess(null);
    }

}

