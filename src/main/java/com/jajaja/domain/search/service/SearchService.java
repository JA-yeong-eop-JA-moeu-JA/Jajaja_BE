package com.jajaja.domain.search.service;

import com.jajaja.domain.search.dto.PagingSearchProductListResponseDto;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.dto.RecentSearchKeywordResponseDto;
import com.jajaja.domain.search.entity.enums.SearchSort;

import java.util.List;

public interface SearchService {
    PopularSearchKeywordsResponseDto getPopularKeywords();
    PagingSearchProductListResponseDto searchProductsByKeyword(Long memberId, String keyword, SearchSort sort, int page, int size);
    List<RecentSearchKeywordResponseDto> getRecentSearchKeywords(Long memberId);
    void deleteSearchKeywordById(Long memberId, Long keywordId);
}
