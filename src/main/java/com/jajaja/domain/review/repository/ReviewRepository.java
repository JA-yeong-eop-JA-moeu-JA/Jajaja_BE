package com.jajaja.domain.review.repository;

import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByOrderProduct(OrderProduct orderProduct);
}
