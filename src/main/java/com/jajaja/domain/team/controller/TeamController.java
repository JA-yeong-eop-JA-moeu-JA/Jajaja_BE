package com.jajaja.domain.team.controller;

import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.dto.response.TeamProductListResponseDto;
import com.jajaja.domain.team.service.TeamCommandService;
import com.jajaja.domain.team.service.TeamQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

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
            description = "장바구니 상품의 '팀 참여하기' 버튼을 누를 시, 팀 참여가 진행됩니다."
    )
    @PostMapping("/carts/join/{productId}")
    public ApiResponse<String> joinTeamInCarts(@Auth Long memberId, @PathVariable Long productId) {
        teamCommandService.joinTeamInCarts(memberId, productId);
        return ApiResponse.onSuccess("장바구니 내 상품의 팀 참여가 완료되었습니다.");
    }

    @Operation(
            summary = "팀 모집 상품 목록 조회 API | by 루비/이송미",
            description = "팀이 생성된 상품들을 최신순으로 조회합니다."
    )
    @GetMapping("/products")
    public ApiResponse<TeamProductListResponseDto> getMatchingTeamProducts(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size
    ) {
        TeamProductListResponseDto response = teamQueryService.getMatchingTeamProducts(page, size);
        return ApiResponse.onSuccess(response);
    }
}
