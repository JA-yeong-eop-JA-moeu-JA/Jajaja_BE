package com.jajaja.domain.search.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PopularSearchKeywordsResponseDto(
        LocalDateTime baseTime,
        List<String> keywords
) {
    public static PopularSearchKeywordsResponseDto of(List<String> keywords) {
        return new PopularSearchKeywordsResponseDto(LocalDateTime.now(), keywords);
    }
}
