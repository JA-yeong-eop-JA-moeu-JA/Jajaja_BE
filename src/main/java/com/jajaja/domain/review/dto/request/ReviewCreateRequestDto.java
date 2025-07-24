package com.jajaja.domain.review.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewCreateRequestDto(
        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 1, message = "1점 이상이어야 합니다.")
        @Max(value = 5, message = "5점 이하로 작성해야 합니다.")
        Byte rating,

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 500, message = "500자 이하로 작성해주세요.")
        String content,

        List<String> imageKeys
) {}
