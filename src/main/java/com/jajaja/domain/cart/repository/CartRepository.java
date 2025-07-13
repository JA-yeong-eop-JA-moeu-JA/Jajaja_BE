package com.jajaja.domain.cart.repository;

import com.jajaja.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query("SELECT c FROM Cart  c " +
	"LEFT JOIN FETCH c.cartProducts cp " +
	"LEFT JOIN FETCH cp.product p " +
	"LEFT JOIN FETCH cp.productOption po " +
	"WHERE c.member.id = :memberId")
	Optional<Cart> findByMemberId(@Param("memberId") Long memberId);
}
