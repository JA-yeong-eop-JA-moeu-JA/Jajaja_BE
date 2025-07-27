package com.jajaja.domain.coupon.repository;

import com.jajaja.domain.cart.entity.QCart;
import com.jajaja.domain.cart.entity.QCartProduct;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.product.entity.category.QProductSubCategory;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponValidationRepositoryImpl implements CouponValidationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findCategoryNamesByCartId(Long cartId) {
        if (cartId == null) {
            return List.of();
        }

        QCart cart = QCart.cart;
        QCartProduct cartProduct = QCartProduct.cartProduct;
        QProduct product = QProduct.product;
        QProductSubCategory productSubCategory = QProductSubCategory.productSubCategory;

        return queryFactory
                .selectDistinct(
                        new CaseBuilder()
                                .when(productSubCategory.subCategory.isNotNull())
                                .then(productSubCategory.subCategory.name.stringValue())
                                .when(productSubCategory.category.isNotNull())
                                .then(productSubCategory.category.name.stringValue())
                                .when(productSubCategory.categoryGroup.isNotNull())
                                .then(productSubCategory.categoryGroup.name.stringValue())
                                .otherwise("UNKNOWN")
                )
                .from(cart)
                .join(cart.cartProducts, cartProduct)
                .join(cartProduct.product, product)
                .join(product.productSubCategories, productSubCategory)
                .where(cart.id.eq(cartId))
                .fetch();
    }

    @Override
    public List<Object[]> findCategoryNamesByProductIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        QProduct product = QProduct.product;
        QProductSubCategory productSubCategory = QProductSubCategory.productSubCategory;

        return queryFactory
                .selectDistinct(
                        product.id,
                        new CaseBuilder()
                                .when(productSubCategory.subCategory.isNotNull())
                                .then(productSubCategory.subCategory.name.stringValue())
                                .when(productSubCategory.category.isNotNull())
                                .then(productSubCategory.category.name.stringValue())
                                .when(productSubCategory.categoryGroup.isNotNull())
                                .then(productSubCategory.categoryGroup.name.stringValue())
                                .otherwise("UNKNOWN")
                )
                .from(product)
                .join(product.productSubCategories, productSubCategory)
                .where(product.id.in(productIds))
                .fetch().stream()
                .map(tuple -> new Object[]{tuple.get(product.id), tuple.get(1, String.class)})
                .toList();
    }
}