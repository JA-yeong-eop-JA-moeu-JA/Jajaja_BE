package com.jajaja.domain.point.service;

import com.jajaja.domain.point.dto.response.PagingPointHistoryResponseDto;
import com.jajaja.domain.point.dto.response.PointBalanceResponseDto;
import com.jajaja.domain.point.dto.response.PointHistoryDto;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryServiceImpl implements PointQueryService {

    private final PointRepository pointRepository;

    @Override
    public PagingPointHistoryResponseDto getPointHistory(Long memberId, Pageable pageable) {
        Page<Point> pointPage = pointRepository.findByMemberId(memberId, pageable);
        List<PointHistoryDto> pointHistoryDtos = pointPage.getContent().stream()
                .map(PointHistoryDto::from)
                .collect(Collectors.toList());
        int pointBalance = pointRepository.findAvailablePointsByMemberId(memberId);
        return PagingPointHistoryResponseDto.of(pointPage, pointBalance, pointHistoryDtos);
    }

    @Override
    public PointBalanceResponseDto getPointBalance(Long memberId) {
        int pointBalance = pointRepository.findAvailablePointsByMemberId(memberId);
        return PointBalanceResponseDto.from(pointBalance);
    }
}
