package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;

public interface MemberQueryService {

    MemberInfoResponseDto getMemberInfo(Long memberId);
}
