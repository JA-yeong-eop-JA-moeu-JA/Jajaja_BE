package com.jajaja.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1653737969L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    public final com.jajaja.domain.coupon.entity.QCoupon coupon;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.jajaja.domain.delivery.entity.QDelivery delivery;

    public final NumberPath<Integer> discountAmount = createNumber("discountAmount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.jajaja.domain.user.entity.QUser member;

    public final ListPath<OrderProduct, QOrderProduct> orderProducts = this.<OrderProduct, QOrderProduct>createList("orderProducts", OrderProduct.class, QOrderProduct.class, PathInits.DIRECT2);

    public final EnumPath<com.jajaja.domain.order.entity.enums.OrderType> orderType = createEnum("orderType", com.jajaja.domain.order.entity.enums.OrderType.class);

    public final EnumPath<com.jajaja.domain.order.entity.enums.PaymentMethod> paymentMethod = createEnum("paymentMethod", com.jajaja.domain.order.entity.enums.PaymentMethod.class);

    public final NumberPath<Integer> pointUsedAmount = createNumber("pointUsedAmount", Integer.class);

    public final NumberPath<Integer> shippingFee = createNumber("shippingFee", Integer.class);

    public final EnumPath<com.jajaja.domain.order.entity.enums.OrderStatus> status = createEnum("status", com.jajaja.domain.order.entity.enums.OrderStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new com.jajaja.domain.coupon.entity.QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
        this.delivery = inits.isInitialized("delivery") ? new com.jajaja.domain.delivery.entity.QDelivery(forProperty("delivery"), inits.get("delivery")) : null;
        this.member = inits.isInitialized("member") ? new com.jajaja.domain.user.entity.QUser(forProperty("member"), inits.get("member")) : null;
    }

}

