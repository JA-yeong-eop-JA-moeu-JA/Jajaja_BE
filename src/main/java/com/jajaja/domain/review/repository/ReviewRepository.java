package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT r
        FROM Review r
        LEFT JOIN r.reviewLikes likes
        WHERE r.product.id = :productId
        GROUP BY r
        ORDER BY COUNT(likes) DESC
    """)
    List<Review> findTop3ByProductIdOrderByLikeCountDesc(@Param("productId") Long productId);
}
