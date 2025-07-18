package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.projection.ProductTotalSalesDto;
import com.jajaja.domain.product.entity.QProductSales;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public Map<Long, Long> findTotalSalesByProductIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }

        QProductSales ps = QProductSales.productSales;

        List<Tuple> rows = queryFactory
                .select(ps.product.id, ps.salesCount.sum())
                .from(ps)
                .where(ps.product.id.in(productIds))
                .groupBy(ps.product.id)
                .fetch();

        return rows.stream()
                .collect(Collectors.toMap(
                        t -> t.get(ps.product.id),
                        t -> {
                            Integer sum = t.get(ps.salesCount.sum());
                            return sum == null ? 0L : sum.longValue();
                        }
                ));
    }
}
