package com.jajaja.domain.review.repository;


import com.jajaja.domain.review.entity.QReview;
import com.jajaja.domain.review.entity.QReviewImage;
import com.jajaja.domain.review.entity.QReviewLike;
import com.jajaja.domain.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findTop3ByProductIdOrderByLikeCountDesc(Long productId) {
        QReview review = QReview.review;
        QReviewLike like = QReviewLike.reviewLike;

        return queryFactory
                .select(review)
                .from(review)
                .leftJoin(review.reviewLikes, like)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .groupBy(review.id)
                .orderBy(like.count().desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<Review> findAllByProductId(Long productId) {
        QReview review = QReview.review;

        return queryFactory
                .selectFrom(review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
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
}
