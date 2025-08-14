package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberCommandService {

    MemberInfoResponseDto updateMemberInfo(Long memberId, MemberProfileUpdateRequest request);

    void acceptTerms(Long memberId, HttpServletResponse response);
}
