package com.jajaja.domain.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearch is a Querydsl query type for Search
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearch extends EntityPathBase<Search> {

    private static final long serialVersionUID = 1955019483L;

    public static final QSearch search = new QSearch("search");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSearch(String variable) {
        super(Search.class, forVariable(variable));
    }

    public QSearch(Path<? extends Search> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearch(PathMetadata metadata) {
        super(Search.class, metadata);
    }

}

