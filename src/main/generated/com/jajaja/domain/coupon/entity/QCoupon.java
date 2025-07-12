package com.jajaja.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1348922199L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final com.jajaja.domain.cart.entity.QCart cart;

    public final EnumPath<com.jajaja.domain.coupon.entity.enums.ConditionType> conditionType = createEnum("conditionType", com.jajaja.domain.coupon.entity.enums.ConditionType.class);

    public final StringPath conditionValues = createString("conditionValues");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.jajaja.domain.coupon.entity.enums.DiscountType> discountType = createEnum("discountType", com.jajaja.domain.coupon.entity.enums.DiscountType.class);

    public final NumberPath<Integer> discountValue = createNumber("discountValue", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> minOrderAmount = createNumber("minOrderAmount", Integer.class);

    public final StringPath name = createString("name");

    public final ListPath<com.jajaja.domain.order.entity.Order, com.jajaja.domain.order.entity.QOrder> orders = this.<com.jajaja.domain.order.entity.Order, com.jajaja.domain.order.entity.QOrder>createList("orders", com.jajaja.domain.order.entity.Order.class, com.jajaja.domain.order.entity.QOrder.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.jajaja.domain.user.entity.UserCoupon, com.jajaja.domain.user.entity.QUserCoupon> userCoupons = this.<com.jajaja.domain.user.entity.UserCoupon, com.jajaja.domain.user.entity.QUserCoupon>createList("userCoupons", com.jajaja.domain.user.entity.UserCoupon.class, com.jajaja.domain.user.entity.QUserCoupon.class, PathInits.DIRECT2);

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new com.jajaja.domain.cart.entity.QCart(forProperty("cart"), inits.get("cart")) : null;
    }

}

