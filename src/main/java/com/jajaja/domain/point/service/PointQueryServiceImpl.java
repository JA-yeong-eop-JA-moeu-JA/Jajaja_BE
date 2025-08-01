package com.jajaja.domain.point.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.point.dto.response.PagingPointHistoryResponseDto;
import com.jajaja.domain.point.dto.response.PointBalanceResponseDto;
import com.jajaja.domain.point.dto.response.PointHistoryDto;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.repository.PointRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
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
    private final MemberRepository memberRepository;
    
    @Override
    public PagingPointHistoryResponseDto getPointHistory(Long memberId, Pageable pageable) {
        Page<Point> pointPage = pointRepository.findByMemberId(memberId, pageable);
        List<PointHistoryDto> pointHistoryDtos = pointPage.getContent().stream()
                .map(PointHistoryDto::from)
                .collect(Collectors.toList());
        int pointBalance = pointRepository.findPointBalanceByMemberId(memberId);
        return PagingPointHistoryResponseDto.of(pointPage, pointBalance, pointHistoryDtos);
    }
    
    @Override
    public PointBalanceResponseDto getPointBalance(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        return PointBalanceResponseDto.from(member.getPoint());
    }
}