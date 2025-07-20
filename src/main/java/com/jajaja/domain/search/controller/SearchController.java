package com.jajaja.domain.search.controller;

import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.entity.enums.SearchSort;
import com.jajaja.domain.search.service.SearchService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "상품 검색 API | by 구름/윤윤지",
            description = "키워드 기반으로 상품을 검색하고, 정렬 기준에 따라 결과를 반환합니다."
    )
    @GetMapping
    public ApiResponse<List<ProductListResponseDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "POPULAR") SearchSort sort) {

        List<ProductListResponseDto> result = searchService.searchProductsByKeyword(keyword, sort);
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
}
