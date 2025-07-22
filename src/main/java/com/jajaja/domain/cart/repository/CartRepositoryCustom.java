package com.jajaja.domain.cart.repository;

import com.jajaja.domain.cart.entity.Cart;
import org.springframework.data.repository.query.Param;

public interface CartRepositoryCustom {
	Cart findByMemberId(@Param("memberId") Long memberId);
}
