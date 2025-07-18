package com.jajaja.domain.product.controller;

import com.jajaja.domain.product.dto.response.CategoryResponseDto;
import com.jajaja.domain.product.dto.response.SubCategoryResponseDto;
import com.jajaja.domain.product.service.CategoryQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @Operation(
            summary = "상위 카테고리 조회 API | by 루비/이송미",
            description = "카테고리 그룹(DEFAULT, BUSINESS)에 해당하는 상위 카테고리 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<List<CategoryResponseDto>> getCategoriesByGroup(@RequestParam String group) {
        List<CategoryResponseDto> response = categoryQueryService.getCategoriesByGroup(group);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "하위 카테고리 조회 API | by 루비/이송미",
            description = "상위 카테고리에 속한 하위 카테고리 목록을 조회합니다."
    )
    @GetMapping("/{categoryId}/subcategories")
    public ApiResponse<List<SubCategoryResponseDto>> getSubCategories(@PathVariable Long categoryId) {
        List<SubCategoryResponseDto> response = categoryQueryService.getSubCategories(categoryId);
        return ApiResponse.onSuccess(response);
    }

}


