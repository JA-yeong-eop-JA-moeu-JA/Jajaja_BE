package com.jajaja.domain.review.repository;


import com.jajaja.domain.review.entity.QReview;
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
                .where(review.product.id.eq(productId))
                .groupBy(review.id)
                .orderBy(like.count().desc())
                .limit(3)
                .fetch();
    }
}
