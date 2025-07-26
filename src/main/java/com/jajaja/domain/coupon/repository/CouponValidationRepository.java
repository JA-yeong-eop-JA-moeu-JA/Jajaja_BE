package com.jajaja.domain.coupon.repository;

import com.jajaja.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponValidationRepository extends JpaRepository<Cart, Long>, CouponValidationRepositoryCustom {
}