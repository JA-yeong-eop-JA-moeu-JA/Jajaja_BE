package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.response.CategorySimpleResponseDto;

import java.util.List;

public interface CategoryQueryRepository {
    List<CategorySimpleResponseDto> findAllByCategoryGroupName(String groupName);
}
