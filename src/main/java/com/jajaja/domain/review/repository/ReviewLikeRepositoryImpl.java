package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.QReviewLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepositoryImpl implements ReviewLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Integer> findReviewIdsLikedByUser(Long userId, List<Integer> reviewIds) {
        QReviewLike rl = QReviewLike.reviewLike;

        return new HashSet<>(
                queryFactory
                        .select(rl.review.id.intValue())
                        .from(rl)
                        .where(
                                rl.member.id.eq(userId),
                                rl.review.id.intValue().in(reviewIds)
                        )
                        .fetch()
        );
    }
}
