package com.jajaja.domain.member.controller;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.member.service.MemberCommandService;
import com.jajaja.domain.member.service.MemberQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(
            summary = "내 정보 조회",
            description = "로그인한 사용자의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ApiResponse<MemberInfoResponseDto> getMyInfo(@Auth Long memberId) {
        MemberInfoResponseDto memberInfoResponseDto = memberQueryService.getMemberInfo(memberId);
        return ApiResponse.onSuccess(memberInfoResponseDto);
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "memberId 사용자의 정보를 수정합니다. \n" +
                    "수정이 필요하지 않은 필드는 null(혹은 미포함)로 요청하면 됩니다."
    )
    @PatchMapping("/{memberId}")
    public ApiResponse<MemberInfoResponseDto> updateMemberInfo(@PathVariable Long memberId,
                                                               @RequestBody @Valid MemberProfileUpdateRequest request) {
        MemberInfoResponseDto updatedMemberInfo = memberCommandService.updateMemberInfo(memberId, request);
        return ApiResponse.onSuccess(updatedMemberInfo);
    }
}
