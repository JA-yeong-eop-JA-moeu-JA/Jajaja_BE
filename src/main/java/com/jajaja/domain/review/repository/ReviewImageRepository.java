package com.jajaja.domain.review.repository;

import com.jajaja.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long>, ReviewImageRepositoryCustom {
}
