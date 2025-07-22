package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.CategoryProductListResponseDto;
import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;
import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import org.springframework.data.domain.Pageable;

public interface ProductQueryService {
    ProductDetailResponseDto getProductDetail(Long memberId, Long productId);
    HomeProductListResponseDto getProductList(Long userId, Long categoryId);
    CategoryProductListResponseDto getProductsBySubCategory(Long subcategoryId, String sort, Pageable pageable);
}
