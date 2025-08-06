package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.S3.service.S3Service;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Override
    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        String profileUrl = s3Service.generateStaticUrl(member.getProfileKeyName());
        return MemberInfoResponseDto.of(member, profileUrl);
    }
}
