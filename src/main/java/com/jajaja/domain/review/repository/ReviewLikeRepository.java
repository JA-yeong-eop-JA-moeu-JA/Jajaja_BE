package com.jajaja.domain.review.repository;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long>, ReviewLikeRepositoryCustom {
    Optional<ReviewLike> findByReviewAndMember(Review review, Member member);
}
