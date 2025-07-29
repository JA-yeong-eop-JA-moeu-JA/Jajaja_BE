package com.jajaja.domain.point.service;

import com.jajaja.domain.point.dto.response.PagingPointHistoryResponseDto;
import com.jajaja.domain.point.dto.response.PointBalanceResponseDto;
import org.springframework.data.domain.Pageable;

public interface PointQueryService {
    
    PagingPointHistoryResponseDto getPointHistory(Long memberId, Pageable pageable);
    
    PointBalanceResponseDto getPointBalance(Long memberId);
}