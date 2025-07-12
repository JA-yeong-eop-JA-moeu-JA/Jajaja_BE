package com.jajaja.domain.product.controller;

import com.jajaja.domain.product.dto.response.CategorySimpleResponseDto;
import com.jajaja.domain.product.service.CategoryQueryService;
import com.jajaja.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @Operation(
            summary = "카테고리 그룹 조회 API | by 루비/이송미",
            description = "카테고리 그룹(BASIC, INDUSTRY)에 해당하는 상위 카테고리 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<List<CategorySimpleResponseDto>> getCategoriesByGroup(@RequestParam String group) {
        List<CategorySimpleResponseDto> response = categoryQueryService.getCategoriesByGroup(group);
        return ApiResponse.onSuccess(response);
    }

}


