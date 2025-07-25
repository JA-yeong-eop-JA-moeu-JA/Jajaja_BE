package com.jajaja.domain.coupon.repository;

import com.jajaja.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponValidationRepository extends JpaRepository<Cart, Long> {

    /**
     * 장바구니에 포함된 상품들의 카테고리 이름을 조회합니다.
     */
    @Query("""
        SELECT DISTINCT CASE\s
            WHEN psc.subCategory IS NOT NULL THEN psc.subCategory.name
            WHEN psc.category IS NOT NULL THEN psc.category.name
            WHEN psc.categoryGroup IS NOT NULL THEN psc.categoryGroup.name
            ELSE 'UNKNOWN'
            END
        FROM Cart c
        JOIN c.cartProducts cp\s
        JOIN cp.product p
        JOIN p.productSubCategories psc
        WHERE c.id = :cartId
       \s""")
    List<String> findCategoryNamesByCartId(@Param("cartId") Long cartId);
    
    /**
     * 여러 상품의 카테고리 이름을 일괄 조회합니다.
     */
    @Query("""
        SELECT p.id as productId,\s
               CASE\s
                   WHEN psc.subCategory IS NOT NULL THEN psc.subCategory.name
                   WHEN psc.category IS NOT NULL THEN psc.category.name
                   WHEN psc.categoryGroup IS NOT NULL THEN psc.categoryGroup.name
                   ELSE 'UNKNOWN'
               END as categoryName
        FROM Product p
        JOIN p.productSubCategories psc
        WHERE p.id IN :productIds\s""")
    List<Object[]> findCategoryNamesByProductIds(@Param("productIds") List<Long> productIds);
}