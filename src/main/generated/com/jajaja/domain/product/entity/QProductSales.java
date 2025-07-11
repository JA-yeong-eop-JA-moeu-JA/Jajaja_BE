package com.jajaja.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductSales is a Querydsl query type for ProductSales
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSales extends EntityPathBase<ProductSales> {

    private static final long serialVersionUID = 461830139L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductSales productSales = new QProductSales("productSales");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final QBusinessCategory businessCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public final NumberPath<Integer> salesCount = createNumber("salesCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProductSales(String variable) {
        this(ProductSales.class, forVariable(variable), INITS);
    }

    public QProductSales(Path<? extends ProductSales> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductSales(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductSales(PathMetadata metadata, PathInits inits) {
        this(ProductSales.class, metadata, inits);
    }

    public QProductSales(Class<? extends ProductSales> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.businessCategory = inits.isInitialized("businessCategory") ? new QBusinessCategory(forProperty("businessCategory")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

