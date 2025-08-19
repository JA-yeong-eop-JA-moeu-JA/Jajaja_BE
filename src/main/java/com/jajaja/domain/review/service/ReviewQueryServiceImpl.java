package com.jajaja.domain.review.service;

import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.review.dto.response.*;
import com.jajaja.domain.review.repository.ReviewImageRepository;
import com.jajaja.domain.review.repository.ReviewLikeRepository;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewCommonService reviewCommonService;

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
    public PagingReviewListResponseDto getReviewList(Long memberId, Long productId, String sort, int page, int size) {
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
        Set<Integer> likedReviewIds = memberId != null
                ? reviewLikeRepository.findReviewIdsLikedByMember(memberId, reviewIds)
                : Set.of();

        List<ReviewItemDto> convertedDtos = reviewCommonService.changeReviewWriterProfile(reviewItemPage.getContent());

        // ReviewListDto로 병합
        List<ReviewListDto> reviewDtos = convertedDtos.stream()
                .map(dto -> new ReviewListDto(
                        dto,
                        likedReviewIds.contains(dto.id()),
                        imageUrlsMap.getOrDefault(dto.id(), List.of())
                ))
                .toList();

        return PagingReviewListResponseDto.of(reviewItemPage, reviewDtos);
    }

    @Override
    public PagingReviewImageListResponseDto getReviewImageList(Long productId, String sort, int page, int size) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Page<ReviewImageListDto> reviewImageListPage;

        switch (sort.toLowerCase()) {
            case "recommend":
                reviewImageListPage = reviewImageRepository.findPagedPhotoListByProductIdOrderByLikeCount(productId, page, size);
                break;
            case "latest":
            default:
                reviewImageListPage = reviewImageRepository.findPagedPhotoListByProductIdOrderByCreatedAt(productId, page, size);
                break;
        }

        List<ReviewImageListDto> reviewImageListDtos = reviewImageListPage.getContent();

        return PagingReviewImageListResponseDto.of(reviewImageListPage, reviewImageListDtos);
    }

    @Override
    public PagingReviewListResponseDto getMyReviewList(Long memberId, int page, int size) {
        Page<ReviewItemDto> reviewItemPage = reviewRepository.findPageByMemberIdOrderByCreatedAt(memberId, page, size);

        List<Integer> reviewIds = reviewItemPage.stream()
                .map(ReviewItemDto::id)
                .toList();

        Map<Integer, List<String>> imageUrlsMap = reviewImageRepository
                .findTop6ImageUrlsGroupedByReviewIds(reviewIds);

        List<ReviewListDto> reviewDtos = reviewItemPage.stream()
                .map(dto -> new ReviewListDto(
                        dto,
                        false,
                        imageUrlsMap.getOrDefault(dto.id(), List.of())
                ))
                .toList();

        return PagingReviewListResponseDto.of(reviewItemPage, reviewDtos);
    }

    @Override
    public PagingReviewListResponseDto getAllReviewList(Long memberId, String sort, int page, int size) {
        Page<ReviewItemDto> reviewItemPage;

        switch (sort.toLowerCase()) {
            case "recommend":
                reviewItemPage = reviewRepository.findPageAllOrderByLikeCount(page, size);
                break;
            case "latest":
            default:
                reviewItemPage = reviewRepository.findPageAllOrderByCreatedAt(page, size);
                break;
        }

        List<Integer> reviewIds = reviewItemPage.stream()
                .map(ReviewItemDto::id)
                .toList();

        Map<Integer, List<String>> imageUrlsMap = reviewImageRepository
                .findTop6ImageUrlsGroupedByReviewIds(reviewIds);

        Set<Integer> likedReviewIds = memberId != null
                ? reviewLikeRepository.findReviewIdsLikedByMember(memberId, reviewIds)
                : Set.of();

        List<ReviewItemDto> convertedDtos = reviewCommonService.changeReviewWriterProfile(reviewItemPage.getContent());

        List<ReviewListDto> reviewDtos = convertedDtos.stream()
                .map(dto -> new ReviewListDto(
                        dto,
                        likedReviewIds.contains(dto.id()),
                        imageUrlsMap.getOrDefault(dto.id(), List.of())
                ))
                .toList();

        return PagingReviewListResponseDto.of(reviewItemPage, reviewDtos);
    }

    @Override
    public PagingReviewableOrderListResponseDto getReviewableProducts(Long memberId, int page, int size) {
        if (memberId == null) {
            throw new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Page<ReviewableOrderItemDto> flatPage = reviewRepository.findPageReviewableByMemberId(memberId, page, size);

        Map<Long, List<ReviewableOrderItemDto>> groupedByOrder = flatPage.getContent().stream()
                .collect(Collectors.groupingBy(ReviewableOrderItemDto::orderId));

        List<ReviewableOrderListDto> orderDtos = groupedByOrder.entrySet().stream()
                .map(entry -> {
                    Long orderId = entry.getKey();
                    List<ReviewableOrderItemDto> items = entry.getValue();
                    LocalDateTime date = items.get(0).orderDate();
                    return ReviewableOrderListDto.of(orderId, date, items);
                })
                .toList();

        return PagingReviewableOrderListResponseDto.of(flatPage, orderDtos);
    }

}
