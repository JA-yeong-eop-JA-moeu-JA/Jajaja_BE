package com.jajaja.domain.product.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubCategory is a Querydsl query type for SubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubCategory extends EntityPathBase<SubCategory> {

    private static final long serialVersionUID = 807676620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubCategory subCategory = new QSubCategory("subCategory");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final QCategory category;

    public final QCategoryGroup categoryGroup;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ProductSubCategory, QProductSubCategory> productSubCategories = this.<ProductSubCategory, QProductSubCategory>createList("productSubCategories", ProductSubCategory.class, QProductSubCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSubCategory(String variable) {
        this(SubCategory.class, forVariable(variable), INITS);
    }

    public QSubCategory(Path<? extends SubCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubCategory(PathMetadata metadata, PathInits inits) {
        this(SubCategory.class, metadata, inits);
    }

    public QSubCategory(Class<? extends SubCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.categoryGroup = inits.isInitialized("categoryGroup") ? new QCategoryGroup(forProperty("categoryGroup")) : null;
    }

}

