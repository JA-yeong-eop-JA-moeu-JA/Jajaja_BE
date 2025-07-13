package com.jajaja.domain.user.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 유저 업종 등록 요청 DTO
 * @param businessCategoryId 업종 ID
 */
public record UserBusinessCategoryRequestDto(
        @NotNull(message = "업종 아이디는 필수입니다.")
        Long businessCategoryId
) {}
