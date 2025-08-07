package com.jajaja.domain.search.service;

import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.dto.RecentSearchKeywordResponseDto;
import com.jajaja.domain.search.entity.enums.SearchSort;

import java.util.List;

public interface SearchService {
    PopularSearchKeywordsResponseDto getPopularKeywords();
    List<ProductListResponseDto> searchProductsByKeyword(Long memberId, String keyword, SearchSort sort);
    List<RecentSearchKeywordResponseDto> getRecentSearchKeywords(Long memberId);
    void deleteSearchKeywordById(Long memberId, Long keywordId);
}
