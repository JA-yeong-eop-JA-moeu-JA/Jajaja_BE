package com.jajaja.domain.product.controller;

import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.service.ProductQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductQueryService productQueryService;

    @Operation(
            summary = "상품 상세 조회 API | by 지지/이지희",
            description = "상품의 상세 페이지를 조회합니다."
    )
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailResponseDto> getProductDetails(@Auth Long userId, @PathVariable Long productId) {
        ProductDetailResponseDto responseDto = productQueryService.getProductDetail(userId, productId);
        return ApiResponse.onSuccess(responseDto);
    }
}
