package com.jajaja.domain.member.controller;

import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.member.service.MemberQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponseDto> getMyInfo(@Auth Long memberId) {
        MemberInfoResponseDto memberInfoResponseDto = memberQueryService.getMemberInfo(memberId);
        return ApiResponse.onSuccess(memberInfoResponseDto);
    }
}
