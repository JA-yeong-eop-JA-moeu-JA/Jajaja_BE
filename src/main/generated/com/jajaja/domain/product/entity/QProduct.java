package com.jajaja.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1577513873L;

    public static final QProduct product = new QProduct("product");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<ProductOption, QProductOption> productOptions = this.<ProductOption, QProductOption>createList("productOptions", ProductOption.class, QProductOption.class, PathInits.DIRECT2);

    public final ListPath<ProductSales, QProductSales> productSalesList = this.<ProductSales, QProductSales>createList("productSalesList", ProductSales.class, QProductSales.class, PathInits.DIRECT2);

    public final ListPath<com.jajaja.domain.product.entity.category.ProductSubCategory, com.jajaja.domain.product.entity.category.QProductSubCategory> productSubCategories = this.<com.jajaja.domain.product.entity.category.ProductSubCategory, com.jajaja.domain.product.entity.category.QProductSubCategory>createList("productSubCategories", com.jajaja.domain.product.entity.category.ProductSubCategory.class, com.jajaja.domain.product.entity.category.QProductSubCategory.class, PathInits.DIRECT2);

    public final NumberPath<Float> rating = createNumber("rating", Float.class);

    public final StringPath store = createString("store");

    public final ListPath<com.jajaja.domain.team.entity.Team, com.jajaja.domain.team.entity.QTeam> teams = this.<com.jajaja.domain.team.entity.Team, com.jajaja.domain.team.entity.QTeam>createList("teams", com.jajaja.domain.team.entity.Team.class, com.jajaja.domain.team.entity.QTeam.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

