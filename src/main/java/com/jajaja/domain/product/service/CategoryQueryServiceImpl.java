package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.CategorySimpleResponseDto;
import com.jajaja.domain.product.repository.CategoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryRepository categoryQueryRepository;

    @Override
    public List<CategorySimpleResponseDto> getCategoriesByGroup(String groupName) {
        return categoryQueryRepository.findAllByCategoryGroupName(groupName);
    }
}
