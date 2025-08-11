package com.jajaja.domain.review.service;

import com.jajaja.domain.review.dto.response.ReviewItemDto;
import com.jajaja.global.S3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommonServiceImpl implements ReviewCommonService {

    private final S3Service s3Service;

    @Override
    public List<ReviewItemDto> changeReviewWriterProfile(List<ReviewItemDto> reviewItemDtoList) {
        return reviewItemDtoList.stream()
                .map(dto -> new ReviewItemDto(
                        dto.id(),
                        dto.memberId(),
                        dto.nickname(),
                        s3Service.generateStaticUrl(dto.profileUrl()),
                        dto.createDate(),
                        dto.rating(),
                        dto.productName(),
                        dto.option(),
                        dto.content(),
                        dto.likeCount(),
                        dto.imagesCount()
                ))
                .toList();
    }

}