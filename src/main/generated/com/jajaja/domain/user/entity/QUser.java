package com.jajaja.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1504215327L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.jajaja.global.common.domain.QBaseEntity _super = new com.jajaja.global.common.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<com.jajaja.domain.notification.entity.Notification, com.jajaja.domain.notification.entity.QNotification> notifications = this.<com.jajaja.domain.notification.entity.Notification, com.jajaja.domain.notification.entity.QNotification>createList("notifications", com.jajaja.domain.notification.entity.Notification.class, com.jajaja.domain.notification.entity.QNotification.class, PathInits.DIRECT2);

    public final StringPath oauthId = createString("oauthId");

    public final EnumPath<com.jajaja.domain.user.entity.enums.OauthType> oauthType = createEnum("oauthType", com.jajaja.domain.user.entity.enums.OauthType.class);

    public final StringPath phone = createString("phone");

    public final ListPath<com.jajaja.domain.point.entity.Point, com.jajaja.domain.point.entity.QPoint> points = this.<com.jajaja.domain.point.entity.Point, com.jajaja.domain.point.entity.QPoint>createList("points", com.jajaja.domain.point.entity.Point.class, com.jajaja.domain.point.entity.QPoint.class, PathInits.DIRECT2);

    public final StringPath profileUrl = createString("profileUrl");

    public final ListPath<com.jajaja.domain.review.entity.ReviewLike, com.jajaja.domain.review.entity.QReviewLike> reviewLikes = this.<com.jajaja.domain.review.entity.ReviewLike, com.jajaja.domain.review.entity.QReviewLike>createList("reviewLikes", com.jajaja.domain.review.entity.ReviewLike.class, com.jajaja.domain.review.entity.QReviewLike.class, PathInits.DIRECT2);

    public final ListPath<com.jajaja.domain.review.entity.Review, com.jajaja.domain.review.entity.QReview> reviews = this.<com.jajaja.domain.review.entity.Review, com.jajaja.domain.review.entity.QReview>createList("reviews", com.jajaja.domain.review.entity.Review.class, com.jajaja.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.jajaja.domain.team.entity.TeamMember, com.jajaja.domain.team.entity.QTeamMember> teamMembers = this.<com.jajaja.domain.team.entity.TeamMember, com.jajaja.domain.team.entity.QTeamMember>createList("teamMembers", com.jajaja.domain.team.entity.TeamMember.class, com.jajaja.domain.team.entity.QTeamMember.class, PathInits.DIRECT2);

    public final ListPath<com.jajaja.domain.team.entity.Team, com.jajaja.domain.team.entity.QTeam> teamsAsLeader = this.<com.jajaja.domain.team.entity.Team, com.jajaja.domain.team.entity.QTeam>createList("teamsAsLeader", com.jajaja.domain.team.entity.Team.class, com.jajaja.domain.team.entity.QTeam.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUserBusinessCategory userBusinessCategory;

    public final ListPath<UserCoupon, QUserCoupon> userCoupons = this.<UserCoupon, QUserCoupon>createList("userCoupons", UserCoupon.class, QUserCoupon.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userBusinessCategory = inits.isInitialized("userBusinessCategory") ? new QUserBusinessCategory(forProperty("userBusinessCategory"), inits.get("userBusinessCategory")) : null;
    }

}

