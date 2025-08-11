package com.jajaja.domain.review.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewItemDto(
        int id,
        Long memberId,
        String nickname,
        String profileUrl,
        LocalDateTime createDate,
        double rating,
        String option,
        String content,
        Long likeCount,
        Long imagesCount,
        String productName
) { }
