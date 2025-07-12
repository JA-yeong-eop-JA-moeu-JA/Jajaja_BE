package com.jajaja.domain.team.controller;

import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.service.TeamCommandService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<TeamCreateResponseDto> createTeam(@Auth Long userId, @PathVariable Long productId) {
        TeamCreateResponseDto responseDto = teamCommandService.createTeam(userId, productId);
        return ApiResponse.onSuccess(responseDto);
    }
}
