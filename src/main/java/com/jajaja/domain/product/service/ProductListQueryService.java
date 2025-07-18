package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;

public interface ProductListQueryService {
    HomeProductListResponseDto getProductList(Long userId, Long categoryId);
}
