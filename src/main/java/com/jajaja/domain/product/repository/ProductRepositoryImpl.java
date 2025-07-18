package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.product.entity.category.QProductSubCategory;
import com.jajaja.domain.review.entity.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findBySubCategoryOrderByCreatedAtDesc(Long subcategoryId, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductSubCategory psc = QProductSubCategory.productSubCategory;

        return queryFactory
                .select(product)
                .from(psc)
                .join(psc.product, product)
                .where(psc.subCategory.id.eq(subcategoryId))
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    @Override
    public List<Product> findBySubCategoryOrderByPriceAsc(Long subcategoryId, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductSubCategory psc = QProductSubCategory.productSubCategory;

        return queryFactory
                .select(product)
                .from(psc)
                .join(psc.product, product)
                .where(psc.subCategory.id.eq(subcategoryId))
                .orderBy(product.price.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    @Override
    public List<Product> findBySubCategoryOrderByReviewCountDesc(Long subcategoryId, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductSubCategory psc = QProductSubCategory.productSubCategory;
        QReview review = QReview.review;

        return queryFactory
                .select(product)
                .from(psc)
                .join(psc.product, product)
                .leftJoin(product.reviews, review)
                .where(psc.subCategory.id.eq(subcategoryId))
                .groupBy(product)
                .orderBy(review.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }
}

