package com.jajaja.domain.review.repository;


import com.jajaja.domain.product.entity.QProductOption;
import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.domain.review.entity.QReview;
import com.jajaja.domain.review.entity.QReviewImage;
import com.jajaja.domain.review.entity.QReviewLike;
import com.jajaja.domain.user.entity.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewItemDto> findTop3ItemByProductIdOrderByLikeCountDesc(Long productId) {
        QReview review = QReview.review;
        QUser member = QUser.user;
        QProductOption option = QProductOption.productOption;
        QReviewLike reviewLike = QReviewLike.reviewLike;
        QReviewImage reviewImage = QReviewImage.reviewImage;

        Expression<Long> likeCountSubquery = JPAExpressions
                .select(reviewLike.count())
                .from(reviewLike)
                .where(reviewLike.review.id.eq(review.id));

        Expression<Long> imageCountSubquery = JPAExpressions
                .select(reviewImage.count())
                .from(reviewImage)
                .where(reviewImage.review.id.eq(review.id));

        return queryFactory
                .select(Projections.constructor(ReviewItemDto.class,
                        review.id.intValue(),
                        member.name,
                        review.createdAt,
                        review.rating.doubleValue(),
                        option.name,
                        review.content,
                        likeCountSubquery,
                        imageCountSubquery
                ))
                .from(review)
                .leftJoin(review.member, member)
                .leftJoin(review.productOption, option)
                .leftJoin(review.reviewLikes, reviewLike)
                .where(review.product.id.eq(productId).and(review.deletedAt.isNull()))
                .groupBy(review.id, member.name, review.createdAt, review.rating, option.name, review.content)
                .orderBy(reviewLike.id.count().desc())
                .limit(3)
                .fetch();
    }

    @Override
    public Long countByProductId(Long productId) {
        QReview review = QReview.review;

        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public Double findAvgRatingByProductId(Long productId) {
        QReview review = QReview.review;

        return queryFactory
                .select(review.rating.avg())
                .from(review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public List<String> findTop6ReviewImageUrlsByProductId(Long productId) {
        QReview review = QReview.review;
        QReviewImage reviewImage = QReviewImage.reviewImage;

        return queryFactory
                .select(reviewImage.imageUrl)
                .from(reviewImage)
                .join(reviewImage.review, review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .limit(6)
                .fetch();
    }

    @Override
    public Page<ReviewItemDto> findPageByProductIdOrderByCreatedAt(Long productId, int page, int size) {
        QReview review = QReview.review;
        QReviewLike reviewLike = QReviewLike.reviewLike;
        QReviewImage reviewImage = QReviewImage.reviewImage;
        QUser member = QUser.user;
        QProductOption productOption = QProductOption.productOption;

        Expression<Long> likeCountSubquery = JPAExpressions
                .select(reviewLike.count())
                .from(reviewLike)
                .where(reviewLike.review.id.eq(review.id));

        Expression<Long> imageCountSubquery = JPAExpressions
                .select(reviewImage.count())
                .from(reviewImage)
                .where(reviewImage.review.id.eq(review.id));

        List<ReviewItemDto> content = queryFactory
                .select(Projections.constructor(ReviewItemDto.class,
                        review.id.intValue(),
                        member.name,
                        review.createdAt,
                        review.rating.doubleValue(),
                        productOption.name,
                        review.content,
                        likeCountSubquery,
                        imageCountSubquery
                ))
                .from(review)
                .join(review.member, member)
                .join(review.productOption, productOption)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .orderBy(review.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(review.count())
                        .from(review)
                        .where(review.product.id.eq(productId)
                                .and(review.deletedAt.isNull()))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    @Override
    public Page<ReviewItemDto> findPageByProductIdOrderByLikeCount(Long productId, int page, int size) {
        QReview review = QReview.review;
        QReviewLike reviewLike = QReviewLike.reviewLike;
        QReviewImage reviewImage = QReviewImage.reviewImage;
        QUser member = QUser.user;
        QProductOption productOption = QProductOption.productOption;

        Expression<Long> likeCountSubquery = JPAExpressions
                .select(reviewLike.count())
                .from(reviewLike)
                .where(reviewLike.review.id.eq(review.id));

        Expression<Long> imageCountSubquery = JPAExpressions
                .select(reviewImage.count())
                .from(reviewImage)
                .where(reviewImage.review.id.eq(review.id));

        List<ReviewItemDto> content = queryFactory
                .select(Projections.constructor(ReviewItemDto.class,
                        review.id.intValue(),
                        member.name,
                        review.createdAt,
                        review.rating.doubleValue(),
                        productOption.name,
                        review.content,
                        likeCountSubquery,
                        imageCountSubquery
                ))
                .from(review)
                .join(review.member, member)
                .join(review.productOption, productOption)
                .leftJoin(review.reviewLikes, reviewLike)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .groupBy(review.id, member.name, productOption.name,
                        review.createdAt, review.rating, review.content)
                .orderBy(reviewLike.id.count().desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(review.count())
                        .from(review)
                        .where(review.product.id.eq(productId)
                                .and(review.deletedAt.isNull()))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

}
