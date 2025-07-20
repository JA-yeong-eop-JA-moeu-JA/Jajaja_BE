package com.jajaja.domain.review.repository;


import com.jajaja.domain.review.entity.QReviewImage;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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
}
