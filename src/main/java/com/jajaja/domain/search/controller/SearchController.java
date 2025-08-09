package com.jajaja.domain.search.controller;

import com.jajaja.domain.search.dto.PagingSearchProductListResponseDto;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.dto.RecentSearchKeywordResponseDto;
import com.jajaja.domain.search.entity.enums.SearchSort;
import com.jajaja.domain.search.service.SearchService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Validated
@Tag(name = "Search API", description = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "상품 검색 API | by 구름/윤윤지",
            description = "키워드 기반으로 상품을 검색하고, 정렬 기준에 따라 결과를 반환합니다."
    )
    @GetMapping
    public ApiResponse<PagingSearchProductListResponseDto> searchProducts(
            @Auth Long memberId,
            @RequestParam @NotBlank(message = "검색어를 입력해주세요.") String keyword,
            @RequestParam(defaultValue = "POPULAR") SearchSort sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PagingSearchProductListResponseDto result = searchService.searchProductsByKeyword(memberId, keyword, sort, page, size);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "인기 검색어 조회 API | by 구름/윤윤지",
            description = "최근 인기 검색어 8개와 기준 시간을 반환합니다."
    )
    @GetMapping("/popular-keywords")
    public ApiResponse<PopularSearchKeywordsResponseDto> getPopularKeywords() {
        PopularSearchKeywordsResponseDto popularKeywords = searchService.getPopularKeywords();
        return ApiResponse.onSuccess(popularKeywords);
    }

    @Operation(summary = "최근 검색어 조회 API | by 구름/윤윤지",
            description = "회원의 최근 검색어를 조회합니다. (ID 포함)"
    )
    @GetMapping("/recent-keywords")
    public ApiResponse<List<RecentSearchKeywordResponseDto>> getRecentSearchKeywords(@Auth Long memberId) {
        List<RecentSearchKeywordResponseDto> keywords = searchService.getRecentSearchKeywords(memberId);
        return ApiResponse.onSuccess(keywords);
    }

    @Operation(summary = "최근 검색어 삭제 API | by 구름/윤윤지",
            description = "최근 검색어 중 특정 검색어를 삭제합니다."
    )
    @DeleteMapping("/recent-keywords/{keywordId}")
    public ApiResponse<Void> deleteSearchKeyword(
            @Auth Long memberId,
            @PathVariable Long keywordId
    ) {
        searchService.deleteSearchKeywordById(memberId, keywordId);
        return ApiResponse.onSuccess(null);
    }
}
