package com.jajaja.domain.search.service;

import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.entity.enums.SearchSort;

import java.util.List;

public interface SearchService {
    List<ProductListResponseDto> searchProductsByKeyword(String keyword, SearchSort sort);
    PopularSearchKeywordsResponseDto getPopularKeywords();
}
