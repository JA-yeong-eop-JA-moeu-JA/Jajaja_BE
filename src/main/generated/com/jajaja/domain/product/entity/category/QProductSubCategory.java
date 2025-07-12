package com.jajaja.domain.product.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductSubCategory is a Querydsl query type for ProductSubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSubCategory extends EntityPathBase<ProductSubCategory> {

    private static final long serialVersionUID = -1523467327L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductSubCategory productSubCategory = new QProductSubCategory("productSubCategory");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final QCategory category;

    public final QCategoryGroup categoryGroup;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.jajaja.domain.product.entity.QProduct product;

    public final QSubCategory subCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProductSubCategory(String variable) {
        this(ProductSubCategory.class, forVariable(variable), INITS);
    }

    public QProductSubCategory(Path<? extends ProductSubCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductSubCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductSubCategory(PathMetadata metadata, PathInits inits) {
        this(ProductSubCategory.class, metadata, inits);
    }

    public QProductSubCategory(Class<? extends ProductSubCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.categoryGroup = inits.isInitialized("categoryGroup") ? new QCategoryGroup(forProperty("categoryGroup")) : null;
        this.product = inits.isInitialized("product") ? new com.jajaja.domain.product.entity.QProduct(forProperty("product")) : null;
        this.subCategory = inits.isInitialized("subCategory") ? new QSubCategory(forProperty("subCategory"), inits.get("subCategory")) : null;
    }

}

