package com.jajaja.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBusinessCategory is a Querydsl query type for BusinessCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBusinessCategory extends EntityPathBase<BusinessCategory> {

    private static final long serialVersionUID = -1517203332L;

    public static final QBusinessCategory businessCategory = new QBusinessCategory("businessCategory");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ProductSales, QProductSales> productSalesList = this.<ProductSales, QProductSales>createList("productSalesList", ProductSales.class, QProductSales.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.jajaja.domain.user.entity.UserBusinessCategory, com.jajaja.domain.user.entity.QUserBusinessCategory> userBusinessCategories = this.<com.jajaja.domain.user.entity.UserBusinessCategory, com.jajaja.domain.user.entity.QUserBusinessCategory>createList("userBusinessCategories", com.jajaja.domain.user.entity.UserBusinessCategory.class, com.jajaja.domain.user.entity.QUserBusinessCategory.class, PathInits.DIRECT2);

    public QBusinessCategory(String variable) {
        super(BusinessCategory.class, forVariable(variable));
    }

    public QBusinessCategory(Path<? extends BusinessCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBusinessCategory(PathMetadata metadata) {
        super(BusinessCategory.class, metadata);
    }

}

