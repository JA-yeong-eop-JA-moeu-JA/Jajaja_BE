package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.ProductOptionResponseDto;

import java.util.List;

public interface ProductOptionQueryService {
    List<ProductOptionResponseDto> getProductOptions(Long productId);
}
