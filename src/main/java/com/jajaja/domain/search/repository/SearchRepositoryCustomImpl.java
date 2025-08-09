package com.jajaja.domain.search.repository;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.search.entity.QSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QSearch search = QSearch.search;

    @Override
    public List<String> findTopSearchKeywords(Pageable pageable) {
        return queryFactory.select(search.name)
                .from(search)
                .orderBy(search.searchCount.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Page<Product> findProductsByKeywordWithPaging(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        List<Product> content = queryFactory.selectFrom(product)
                .where(product.name.containsIgnoreCase(keyword)
                        .or(product.store.containsIgnoreCase(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(product.count())
                .from(product)
                .where(product.name.containsIgnoreCase(keyword)
                        .or(product.store.containsIgnoreCase(keyword)))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
