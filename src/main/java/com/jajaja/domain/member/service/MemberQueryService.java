package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;

import java.util.List;

public interface MemberQueryService {

    MemberInfoResponseDto getMemberInfo(Long memberId);
    List<MemberInfoResponseDto> getMemberInfos(List<Long> memberIds);
}
