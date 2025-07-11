package com.jajaja.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserBusinessCategory is a Querydsl query type for UserBusinessCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBusinessCategory extends EntityPathBase<UserBusinessCategory> {

    private static final long serialVersionUID = -2024668705L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserBusinessCategory userBusinessCategory = new QUserBusinessCategory("userBusinessCategory");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final com.jajaja.domain.product.entity.QBusinessCategory businessCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserBusinessCategory(String variable) {
        this(UserBusinessCategory.class, forVariable(variable), INITS);
    }

    public QUserBusinessCategory(Path<? extends UserBusinessCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserBusinessCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserBusinessCategory(PathMetadata metadata, PathInits inits) {
        this(UserBusinessCategory.class, metadata, inits);
    }

    public QUserBusinessCategory(Class<? extends UserBusinessCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.businessCategory = inits.isInitialized("businessCategory") ? new com.jajaja.domain.product.entity.QBusinessCategory(forProperty("businessCategory")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

