package com.jajaja.domain.cart.repository;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.QCart;
import com.jajaja.domain.cart.entity.QCartProduct;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.product.entity.QProductOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl  implements CartRepositoryCustom{
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public Cart findByMemberId(Long memberId) {
		QCart cart = QCart.cart;
		QCartProduct cartProduct = QCartProduct.cartProduct;
		QProduct product = QProduct.product;
		QProductOption productOption = QProductOption.productOption;
		
		return queryFactory
				.selectFrom(cart)
				.leftJoin(cart.cartProducts, cartProduct).fetchJoin()
				.leftJoin(cartProduct.product, product).fetchJoin()
				.leftJoin(cartProduct.productOption, productOption).fetchJoin()
				.where(cart.member.id.eq(memberId))
				.fetchOne();
	}
}
