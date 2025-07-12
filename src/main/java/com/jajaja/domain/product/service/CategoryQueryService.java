package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.CategoryResponseDto;
import com.jajaja.domain.product.dto.response.SubCategoryResponseDto;

import java.util.List;

public interface CategoryQueryService {
    List<CategoryResponseDto> getCategoriesByGroup(String groupName);

    List<SubCategoryResponseDto> getSubCategories(Long categoryId);
}
