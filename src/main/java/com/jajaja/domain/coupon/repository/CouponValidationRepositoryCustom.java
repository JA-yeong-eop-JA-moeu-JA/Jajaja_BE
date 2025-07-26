package com.jajaja.domain.coupon.repository;

import java.util.List;

public interface CouponValidationRepositoryCustom {
    List<String> findCategoryNamesByCartId(Long cartId);
    List<Object[]> findCategoryNamesByProductIds(List<Long> productIds);
}