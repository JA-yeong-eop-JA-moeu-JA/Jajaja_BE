package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;

public interface MemberCommandService {

    MemberInfoResponseDto updateMemberInfo(Long memberId, MemberProfileUpdateRequest request);
}
