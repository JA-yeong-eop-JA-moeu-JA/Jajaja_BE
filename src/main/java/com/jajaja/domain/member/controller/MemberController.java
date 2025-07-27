package com.jajaja.domain.member.controller;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.member.service.MemberCommandService;
import com.jajaja.domain.member.service.MemberQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponseDto> getMyInfo(@Auth Long memberId) {
        MemberInfoResponseDto memberInfoResponseDto = memberQueryService.getMemberInfo(memberId);
        return ApiResponse.onSuccess(memberInfoResponseDto);
    }

    @PatchMapping("/{memberId}")
    public ApiResponse<MemberInfoResponseDto> updateMemberInfo(@PathVariable Long memberId,
                                                               @RequestBody @Valid MemberProfileUpdateRequest request) {
        MemberInfoResponseDto updatedMemberInfo = memberCommandService.updateMemberInfo(memberId, request);
        return ApiResponse.onSuccess(updatedMemberInfo);
    }
}
