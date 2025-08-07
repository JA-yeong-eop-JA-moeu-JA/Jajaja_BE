package com.jajaja.domain.search.dto;

public record RecentSearchKeywordResponseDto(Long id, String keyword) {
    public static RecentSearchKeywordResponseDto of(Long id, String keyword) {
        return new RecentSearchKeywordResponseDto(id, keyword);
    }
}
