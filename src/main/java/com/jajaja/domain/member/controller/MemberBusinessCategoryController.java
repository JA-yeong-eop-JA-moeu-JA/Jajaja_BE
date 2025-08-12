package com.jajaja.domain.member.controller;

import com.jajaja.domain.member.dto.MemberBusinessCategoryRequestDto;
import com.jajaja.domain.member.service.MemberBusinessCategoryCommandService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
@Tag(name = "Member API", description = "회원 업종 관련 API")
public class MemberBusinessCategoryController {

    private final MemberBusinessCategoryCommandService memberBusinessCategoryCommandService;


    /**
     * 멤버 업종 등록 API
     * @param memberId 멤버 ID
     * @param dto 업종 등록 요청 DTO
     * @return 200 OK
     */
    @Operation(
            summary = "유저 업종 등록 API | by 구름/윤윤지",
            description = "온보딩 단계에서 유저가 선택한 업종을 등록합니다."
    )
    @PostMapping("/")
    public ApiResponse<Void> registerMemberBusinessCategory(
            @Auth Long memberId,
            @RequestBody @Valid MemberBusinessCategoryRequestDto dto
    ) {
        memberBusinessCategoryCommandService.registerMemberBusinessCategory(memberId, dto);
        return ApiResponse.onSuccess(null);
    }

}

