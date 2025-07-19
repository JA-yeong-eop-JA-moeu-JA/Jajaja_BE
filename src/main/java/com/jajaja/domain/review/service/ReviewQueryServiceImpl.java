package com.jajaja.domain.review.service;

import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.review.dto.response.PagingReviewListResponseDto;
import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;
import com.jajaja.domain.review.dto.response.ReviewListDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewImage;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewBriefResponseDto getReviewBriefInfo(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Integer reviewCount = Math.toIntExact(reviewRepository.countByProductId(productId));
        double avgRating = Optional.ofNullable(reviewRepository.findAvgRatingByProductId(productId)).orElse(0.0);
        List<String> imageUrls = reviewRepository.findTop6ReviewImageUrlsByProductId(productId);

        return ReviewBriefResponseDto.of(reviewCount, avgRating, imageUrls);
    }

    @Override
    public PagingReviewListResponseDto getReviewList(Long userId, Long productId, String sortType, Pageable pageable) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Page<Review> reviewPage;

        switch (sortType.toLowerCase()) {
            case "recommend":
                reviewPage = reviewRepository.findPageByProductIdOrderByLikeCount(productId, pageable);
                break;
            case "latest":
            default:
                reviewPage = reviewRepository.findPageByProductIdOrderByCreatedAt(productId, pageable);
                break;
        }

        List<ReviewListDto> reviewDtos = reviewPage.stream()
                .map(review -> {
                    boolean isLike = false;
                    if (userId != null) {
                        isLike = review.getReviewLikes().stream()
                                .anyMatch(like -> like.getMember().getId().equals(userId));
                    }
                    List<String> imageUrls = review.getReviewImages().stream()
                            .map(ReviewImage::getImageUrl)
                            .toList();
                    return ReviewListDto.from(review, isLike, imageUrls);
                })
                .toList();

        return PagingReviewListResponseDto.of(reviewPage, reviewDtos);
    }

}
