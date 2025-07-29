package com.jajaja.domain.team.dto.response;

import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamProductListResponseDto(
        PageResponse page,
        List<TeamProductItemResponseDto> teams
) {}