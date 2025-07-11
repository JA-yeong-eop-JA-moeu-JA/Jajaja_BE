package com.jajaja.domain.product.controller;

import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.dto.response.ProductOptionResponseDto;
import com.jajaja.domain.product.service.ProductOptionQueryService;
import com.jajaja.domain.product.service.ProductQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductOptionQueryService productOptionQueryService;

    @Operation(
            summary = "상품 상세 조회 API | by 지지/이지희",
            description = "상품의 상세 페이지를 조회합니다."
    )
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailResponseDto> getProductDetails(@Auth Long userId, @PathVariable Long productId) {
        ProductDetailResponseDto responseDto = productQueryService.getProductDetail(userId, productId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(
            summary = "상품 옵션 조회 API | by 지지/이지희",
            description = "상품의 옵션 리스트를 조회합니다."
    )
    @GetMapping("/{productId}/options")
    public ApiResponse<List<ProductOptionResponseDto>> getProductOptions(@PathVariable Long productId) {
       List<ProductOptionResponseDto> responseDto = productOptionQueryService.getProductOptions(productId);
        return ApiResponse.onSuccess(responseDto);
    }
}
