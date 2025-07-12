package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.CategoryResponseDto;
import com.jajaja.domain.product.dto.response.SubCategoryResponseDto;
import com.jajaja.domain.product.repository.CategoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryRepository categoryQueryRepository;

    @Override
    public List<CategoryResponseDto> getCategoriesByGroup(String groupName) {
        return categoryQueryRepository.findAllByCategoryGroupName(groupName);
    }

    @Override
    public List<SubCategoryResponseDto> getSubCategories(Long categoryId) {
        return categoryQueryRepository.findSubCategoriesByCategoryId(categoryId);
    }

}
