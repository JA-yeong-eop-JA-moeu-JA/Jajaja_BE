package com.jajaja.domain.review.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.order.repository.OrderProductRepository;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.review.dto.request.ReviewCreateRequestDto;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.entity.ReviewImage;
import com.jajaja.domain.review.repository.ReviewImageRepository;
import com.jajaja.domain.review.repository.ReviewRepository;
import com.jajaja.global.S3.service.S3Service;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.jajaja.global.apiPayload.exception.custom.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final S3Service s3Service;

    @Override
    public Long createReview(Long memberId, Long productId, ReviewCreateRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException(ErrorStatus.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        boolean isPurchased = orderProductRepository.existsByOrderMemberIdAndProductId(memberId, productId);
        if (!isPurchased) {
            throw new BadRequestException(ErrorStatus.REVIEW_NOT_ALLOWED);
        }

        Review review = Review.builder()
                .member(member)
                .product(product)
                .rating(dto.rating())
                .content(dto.content())
                .build();
        reviewRepository.save(review);

        // 이미지 처리
        List<String> imageKeys = dto.imageKeys();
        if (imageKeys != null && !imageKeys.isEmpty()) {
            if (imageKeys.size() > 6) {
                throw new BadRequestException(ErrorStatus.REVIEW_TOO_MANY_IMAGES);
            }

            for (String key : imageKeys) {
                if (!s3Service.doesKeyExist(key)) {
                    throw new BadRequestException(ErrorStatus.INVALID_IMAGE_KEY);
                }
            }

            List<ReviewImage> images = imageKeys.stream()
                    .map(key -> ReviewImage.builder()
                            .review(review)
                            .imageUrl(s3Service.generateStaticUrl(key))
                            .build())
                    .toList();

            reviewImageRepository.saveAll(images);
        }

        return review.getId();
    }

}