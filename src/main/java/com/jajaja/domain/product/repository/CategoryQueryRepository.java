package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.response.CategoryResponseDto;
import com.jajaja.domain.product.dto.response.SubCategoryResponseDto;

import java.util.List;

public interface CategoryQueryRepository {
    List<CategoryResponseDto> findAllByCategoryGroupName(String groupName);

    List<SubCategoryResponseDto> findSubCategoriesByCategoryId(Long categoryId);
}
