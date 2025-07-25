package com.jajaja.domain.product.controller;

import com.jajaja.domain.product.dto.response.CategoryProductListResponseDto;
import com.jajaja.domain.product.dto.response.HomeProductListResponseDto;
import com.jajaja.domain.product.dto.response.ProductDetailResponseDto;
import com.jajaja.domain.product.dto.response.ProductOptionResponseDto;
import com.jajaja.domain.product.service.ProductOptionQueryService;
import com.jajaja.domain.product.service.ProductQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import com.jajaja.global.config.security.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductOptionQueryService productOptionQueryService;

    @Operation(
            summary = "홈 상품 리스트 조회 API | by 구름/윤윤지",
            description = "추천 상품, 인기 상품, 신상품을 각각 8개씩 조회합니다. " +
                    "회원은 자동으로 업종이 조회되며, 비회원은 업종 ID를 쿼리 파라미터로 전달해야 합니다."
    )
    @GetMapping("")
    public ApiResponse<HomeProductListResponseDto> getHomeProducts(
            @Auth Long userId,
            @RequestParam(required = false) Long categoryId
    ) {
        HomeProductListResponseDto response = productQueryService.getProductList(userId, categoryId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "하위 카테고리 상품 목록 조회 API | by 루비/이송미",
            description = "상품을 정렬 기준(인기순 | 최신상품순 | 낮은 가격 순 | 리뷰 많은 순)에 따라 조회합니다."
    )
    @GetMapping("/categories/{subcategoryId}/products")
    public ApiResponse<CategoryProductListResponseDto> getProductsBySubCategory(
            @Parameter(description = "하위 카테고리 ID", required = true)
            @PathVariable Long subcategoryId,

            @Parameter(description = "정렬 기준 (POPULAR | NEW | PRICE_ASC | REVIEW)", example = "NEW")
            @RequestParam(required = false, defaultValue = "NEW") String sort,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        CategoryProductListResponseDto response = productQueryService.getProductsBySubCategory(subcategoryId, sort, pageable);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "상품 상세 조회 API | by 지지/이지희",
            description = "상품의 상세 페이지를 조회합니다."
    )
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailResponseDto> getProductDetails(@Auth Long memberId, @PathVariable Long productId) {
        ProductDetailResponseDto responseDto = productQueryService.getProductDetail(memberId, productId);
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
