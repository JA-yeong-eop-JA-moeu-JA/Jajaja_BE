package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;

public interface ProductQueryService {
    ProductDetailResponseDto getProductDetail(Long userId, Long productId);
}
