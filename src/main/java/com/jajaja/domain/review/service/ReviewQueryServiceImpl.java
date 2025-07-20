package com.jajaja.domain.review.service;

import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.review.dto.response.PagingReviewListResponseDto;
import com.jajaja.domain.review.dto.response.ReviewBriefResponseDto;
import com.jajaja.domain.review.dto.response.ReviewListDto;
import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.domain.review.repository.ReviewImageRepository;
import com.jajaja.domain.review.repository.ReviewLikeRepository;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Override
    public ReviewBriefResponseDto getReviewBriefInfo(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Integer reviewCount = Math.toIntExact(reviewRepository.countByProductId(productId));
        double avgRating = Optional.ofNullable(reviewRepository.findAvgRatingByProductId(productId))
                .map(rating -> Math.round(rating * 10.0) / 10.0)
                .orElse(0.0);
        List<String> imageUrls = reviewRepository.findTop6ReviewImageUrlsByProductId(productId);

        return ReviewBriefResponseDto.of(reviewCount, avgRating, imageUrls);
    }

    @Override
    public PagingReviewListResponseDto getReviewList(Long userId, Long productId, String sort, int page, int size) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Page<ReviewItemDto> reviewItemPage;

        switch (sort.toLowerCase()) {
            case "recommend":
                reviewItemPage = reviewRepository.findPageByProductIdOrderByLikeCount(productId, page, size);
                break;
            case "latest":
            default:
                reviewItemPage = reviewRepository.findPageByProductIdOrderByCreatedAt(productId, page, size);
                break;
        }

        List<Integer> reviewIds = reviewItemPage.stream()
                .map(ReviewItemDto::id)
                .toList();

        // imageUrls 조회
        Map<Integer, List<String>> imageUrlsMap = reviewImageRepository
                .findTop6ImageUrlsGroupedByReviewIds(reviewIds);

        // isLike 조회
        Set<Integer> likedReviewIds = userId != null
                ? reviewLikeRepository.findReviewIdsLikedByUser(userId, reviewIds)
                : Set.of();

        // ReviewListDto로 병합
        List<ReviewListDto> reviewDtos = reviewItemPage.stream()
                .map(dto -> new ReviewListDto(
                        dto,
                        likedReviewIds.contains(dto.id()),
                        imageUrlsMap.getOrDefault(dto.id(), List.of())
                ))
                .toList();

        return PagingReviewListResponseDto.of(reviewItemPage, reviewDtos);
    }

}
