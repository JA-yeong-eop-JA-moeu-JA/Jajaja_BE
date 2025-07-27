package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
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
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Override
    public MemberInfoResponseDto updateMemberInfo(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        if (request.name() != null) member.updateName(request.name());
        if (request.phone() != null) member.updatePhone(request.phone());
        if (request.profileKeyName() != null) {
            String imageUrl = s3Service.generateStaticUrl(request.profileKeyName());
            member.updateProfileUrl(imageUrl);
        }
        return MemberInfoResponseDto.from(member);
    }
}
