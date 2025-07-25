package com.jajaja.domain.member.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 유저 업종 등록 요청 DTO
 * @param businessCategoryId 업종 ID
 */
public record MemberBusinessCategoryRequestDto(
        @NotNull(message = "업종 아이디는 필수입니다.")
        Long businessCategoryId
) {}
