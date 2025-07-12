package com.jajaja.domain.product.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategoryGroup is a Querydsl query type for CategoryGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryGroup extends EntityPathBase<CategoryGroup> {

    private static final long serialVersionUID = 1576149007L;

    public static final QCategoryGroup categoryGroup = new QCategoryGroup("categoryGroup");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final ListPath<Category, QCategory> categories = this.<Category, QCategory>createList("categories", Category.class, QCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.jajaja.domain.product.entity.enums.CategoryGroupName> name = createEnum("name", com.jajaja.domain.product.entity.enums.CategoryGroupName.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCategoryGroup(String variable) {
        super(CategoryGroup.class, forVariable(variable));
    }

    public QCategoryGroup(Path<? extends CategoryGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryGroup(PathMetadata metadata) {
        super(CategoryGroup.class, metadata);
    }

}

