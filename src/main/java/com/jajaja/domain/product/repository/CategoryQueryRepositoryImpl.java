package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.response.CategorySimpleResponseDto;
import com.jajaja.domain.product.entity.category.QCategory;
import com.jajaja.domain.product.entity.category.QCategoryGroup;
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
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QCategory category = QCategory.category;
    private final QCategoryGroup categoryGroup = QCategoryGroup.categoryGroup;

    @Override
    public List<CategorySimpleResponseDto> findAllByCategoryGroupName(String groupName) {
        CategoryGroupName parsedGroupName = parseGroupName(groupName);

        return queryFactory
                .select(Projections.constructor(
                        CategorySimpleResponseDto.class,
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

}

