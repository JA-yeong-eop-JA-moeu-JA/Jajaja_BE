package com.jajaja.domain.review.repository;


import com.jajaja.domain.product.entity.QProductOption;
import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.domain.review.entity.QReview;
import com.jajaja.domain.review.entity.QReviewImage;
import com.jajaja.domain.review.entity.QReviewLike;
import com.jajaja.domain.member.entity.QMember;
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
    private final QReview review = QReview.review;
    private final QMember member = QMember.member;
    private final QProductOption option = QProductOption.productOption;
    private final QReviewLike reviewLike = QReviewLike.reviewLike;
    private final QReviewImage reviewImage = QReviewImage.reviewImage;

    @Override
    public List<ReviewItemDto> findTop3ItemByProductIdOrderByLikeCountDesc(Long productId) {
        return queryFactory
                .select(reviewItemDtoProjection())
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
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public Double findAvgRatingByProductId(Long productId) {
        return queryFactory
                .select(review.rating.avg())
                .from(review)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .fetchOne();
    }

    @Override
    public List<String> findTop6ReviewImageUrlsByProductId(Long productId) {
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
        List<ReviewItemDto> content = queryFactory
                .select(reviewItemDtoProjection())
                .from(review)
                .join(review.member, member)
                .leftJoin(review.productOption, option)
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
        List<ReviewItemDto> content = queryFactory
                .select(reviewItemDtoProjection())
                .from(review)
                .join(review.member, member)
                .leftJoin(review.productOption, option)
                .leftJoin(review.reviewLikes, reviewLike)
                .where(review.product.id.eq(productId)
                        .and(review.deletedAt.isNull()))
                .groupBy(review.id, member.name, option.name,
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

    /**
     * 리뷰에 대한 좋아요 수를 계산하는 서브쿼리 Expression 생성
     *
     * @param review 대상 리뷰
     * @return 해당 리뷰에 대한 좋아요 수 Expression
     */
    private Expression<Long> likeCountExpression(QReview review) {
        return JPAExpressions.select(reviewLike.count())
                .from(reviewLike)
                .where(reviewLike.review.id.eq(review.id));
    }

    /**
     * 리뷰에 포함된 이미지 수를 계산하는 서브쿼리 Expression 생성
     *
     * @param review 대상 리뷰
     * @return 해당 리뷰에 포함된 이미지 수 Expression
     */
    private Expression<Long> imageCountExpression(QReview review) {
        return JPAExpressions.select(reviewImage.count())
                .from(reviewImage)
                .where(reviewImage.review.id.eq(review.id));
    }

    /**
     * ReviewItemDto 객체로 매핑하기 위한 QueryDSL Projection 생성
     *
     * @return ReviewItemDto 생성에 필요한 Projection
     */
    private Expression<ReviewItemDto> reviewItemDtoProjection() {
        return Projections.constructor(ReviewItemDto.class,
                review.id.intValue(),
                member.name,
                review.createdAt,
                review.rating.doubleValue(),
                option.name,
                review.content,
                likeCountExpression(review),
                imageCountExpression(review)
        );
    }
}
