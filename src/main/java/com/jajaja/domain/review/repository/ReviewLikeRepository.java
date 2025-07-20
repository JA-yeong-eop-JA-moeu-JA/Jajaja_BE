package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long>, ReviewLikeRepositoryCustom {
}
