package com.jajaja.domain.team.controller;

import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.service.TeamCommandService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;

    @Operation(
            summary = "팀 생성 API | by 지지/이지희",
            description = "‘팀 생성하기 버튼’을 통해 해당 상품의 팀을 생성합니다. 해당 유저는 만들어진 팀의 리더가 됩니다."
    )
    @PostMapping("/{productId}")
    public ApiResponse<TeamCreateResponseDto> createTeam(@Auth Long memberId, @PathVariable Long productId) {
        TeamCreateResponseDto responseDto = teamCommandService.createTeam(memberId, productId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(
            summary = "팀 참여 API | by 지지/이지희",
            description = "‘참여 버튼’을 통해 해당 팀에 참여하는 기능입니다."
    )
    @PostMapping("/join/{teamId}")
    public ApiResponse<String> joinTeam(@Auth Long memberId, @PathVariable Long teamId) {
        teamCommandService.joinTeam(memberId, teamId);
        return ApiResponse.onSuccess("팀 참여가 완료되었습니다.");
    }

    @Operation(
            summary = "장바구니 상품 팀 참여 API | by 지지/이지희",
            description = "장바구니 상품의 '팀 참여하기' 버튼을 누를 시, 팀 참여가 진행됩니다.")
    @PostMapping("/carts/join/{productId}")
    public ApiResponse<String> joinTeamInCarts(@Auth Long memberId, @PathVariable Long productId) {
        teamCommandService.joinTeamInCarts(memberId, productId);
        return ApiResponse.onSuccess("장바구니 내 상품의 팀 참여가 완료되었습니다.");
    }
}
