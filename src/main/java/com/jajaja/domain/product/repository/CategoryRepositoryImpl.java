package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.response.CategoryResponseDto;
import com.jajaja.domain.product.dto.response.SubCategoryResponseDto;
import com.jajaja.domain.product.entity.category.QCategory;
import com.jajaja.domain.product.entity.category.QCategoryGroup;
import com.jajaja.domain.product.entity.category.QSubCategory;
import com.jajaja.domain.product.entity.enums.CategoryGroupName;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.GeneralException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QCategory category = QCategory.category;
    private final QCategoryGroup categoryGroup = QCategoryGroup.categoryGroup;
    private final QSubCategory subCategory = QSubCategory.subCategory;

    @Override
    public List<CategoryResponseDto> findAllByCategoryGroupName(String groupName) {
        CategoryGroupName parsedGroupName = parseGroupName(groupName);

        return queryFactory
                .select(Projections.constructor(
                        CategoryResponseDto.class,
                        category.id,
                        category.name))
                .from(category)
                .join(category.categoryGroup, categoryGroup)
                .where(categoryGroup.name.eq(parsedGroupName))
                .fetch();
    }

    private CategoryGroupName parseGroupName(String groupName) {
        try {
            return CategoryGroupName.valueOf(groupName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.INVALID_CATEGORY_GROUP);
        }
    }

    @Override
    public List<SubCategoryResponseDto> findSubCategoriesByCategoryId(Long categoryId) {

        boolean categoryExists = queryFactory
                .selectOne()
                .from(category)
                .where(category.id.eq(categoryId))
                .fetchFirst() != null;

        if (!categoryExists) {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        return queryFactory
                .select(Projections.constructor(
                        SubCategoryResponseDto.class,
                        subCategory.id,
                        subCategory.name
                ))
                .from(subCategory)
                .where(subCategory.category.id.eq(categoryId))
                .fetch();
    }

}

