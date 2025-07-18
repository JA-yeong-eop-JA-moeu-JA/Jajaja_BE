package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.projection.ProductTotalSalesDto;
import com.jajaja.domain.product.entity.QProductSales;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductSalesRepositoryImpl implements ProductSalesRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductTotalSalesDto> findTopProductsByTotalSales(Pageable pageable) {
        QProductSales ps = QProductSales.productSales;

        return queryFactory
                .select(Projections.constructor(ProductTotalSalesDto.class,
                        ps.product,
                        ps.salesCount.sum()))
                .from(ps)
                .groupBy(ps.product)
                .orderBy(ps.salesCount.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
