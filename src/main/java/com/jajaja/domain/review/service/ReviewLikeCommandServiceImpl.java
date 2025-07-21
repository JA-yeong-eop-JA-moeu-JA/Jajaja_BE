package com.jajaja.domain.review.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.review.dto.response.ReviewLikeResponseDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewLike;
import com.jajaja.domain.review.repository.ReviewLikeRepository;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewLikeCommandServiceImpl implements ReviewLikeCommandService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    public ReviewLikeResponseDto patchReviewLike(Long memberId, Long reviewId) {
        if (memberId == null) {
            throw new BadRequestException(ErrorStatus.MEMBER_REQUIRED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.REVIEW_NOT_FOUND));

        Optional<ReviewLike> existing = reviewLikeRepository.findByReviewAndMember(review, member);

        if (existing.isPresent()) {
            reviewLikeRepository.delete(existing.get());
            return ReviewLikeResponseDto.of(review, false);
        } else {
            ReviewLike like = ReviewLike.builder()
                    .review(review)
                    .member(member)
                    .build();
            reviewLikeRepository.save(like);
            return ReviewLikeResponseDto.of(review, true);
        }
    }


}
