package com.jajaja.domain.review.repository;


import com.jajaja.domain.review.dto.response.ReviewImageListDto;
import com.jajaja.domain.review.entity.QReview;
import com.jajaja.domain.review.entity.QReviewImage;
import com.jajaja.domain.review.entity.QReviewLike;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewImageRepositoryImpl implements ReviewImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Integer, List<String>> findTop6ImageUrlsGroupedByReviewIds(List<Integer> reviewIds) {
        QReviewImage ri = QReviewImage.reviewImage;

        List<Tuple> results = queryFactory
                .select(ri.review.id.intValue(), ri.imageUrl)
                .from(ri)
                .where(ri.review.id.intValue().in(reviewIds))
                .orderBy(ri.review.id.intValue().asc(), ri.createdAt.asc())
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(ri.review.id.intValue()),
                        Collectors.collectingAndThen(
                                Collectors.mapping(t -> t.get(ri.imageUrl), Collectors.toList()),
                                list -> list.stream().limit(6).toList()
                        )
                ));
    }

    @Override
    public Page<ReviewImageListDto> findPagedPhotoListByProductIdOrderByLikeCount(Long productId, Integer page, Integer size) {
        QReviewImage reviewImage = QReviewImage.reviewImage;
        QReview review = QReview.review;
        QReviewLike reviewLike = QReviewLike.reviewLike;

        NumberExpression<Long> likeCount = Expressions.asNumber(
                JPAExpressions
                        .select(reviewLike.count())
                        .from(reviewLike)
                        .where(reviewLike.review.eq(reviewImage.review))
        );

        List<ReviewImageListDto> content = queryFactory
                .select(Projections.constructor(
                        ReviewImageListDto.class,
                        reviewImage.id.intValue(),
                        review.id.intValue(),
                        reviewImage.imageUrl,
                        review.createdAt
                ))
                .from(reviewImage)
                .join(reviewImage.review, review)
                .where(review.product.id.eq(productId), review.deletedAt.isNull())
                .orderBy(likeCount.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(reviewImage.count())
                        .from(reviewImage)
                        .join(reviewImage.review, review)
                        .where(review.product.id.eq(productId), review.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    @Override
    public Page<ReviewImageListDto> findPagedPhotoListByProductIdOrderByCreatedAt(Long productId, Integer page, Integer size) {
        QReviewImage reviewImage = QReviewImage.reviewImage;
        QReview review = QReview.review;

        List<ReviewImageListDto> content = queryFactory
                .select(Projections.constructor(
                        ReviewImageListDto.class,
                        reviewImage.id.intValue(),
                        review.id.intValue(),
                        reviewImage.imageUrl,
                        review.createdAt
                ))
                .from(reviewImage)
                .join(reviewImage.review, review)
                .where(review.product.id.eq(productId), review.deletedAt.isNull())
                .orderBy(review.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory
                        .select(reviewImage.count())
                        .from(reviewImage)
                        .join(reviewImage.review, review)
                        .where(review.product.id.eq(productId), review.deletedAt.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

}
