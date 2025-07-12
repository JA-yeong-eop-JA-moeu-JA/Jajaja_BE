package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.CategorySimpleResponseDto;

import java.util.List;

public interface CategoryQueryService {
    List<CategorySimpleResponseDto> getCategoriesByGroup(String groupName);
}
