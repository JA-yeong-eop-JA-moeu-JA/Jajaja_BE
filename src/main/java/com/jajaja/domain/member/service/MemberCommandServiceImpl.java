package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.request.MemberProfileUpdateRequest;
import com.jajaja.domain.member.dto.response.MemberInfoResponseDto;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.S3.service.S3Service;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.jajaja.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final JwtProvider jwtProvider;

    @Override
    public MemberInfoResponseDto updateMemberInfo(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        if (request.name() != null) member.updateName(request.name());
        if (request.phone() != null) member.updatePhone(request.phone());
        if (request.profileKeyName() != null) {
            if (request.profileKeyName().isBlank()) {
                member.updateProfileKeyName("default-profile-image.png");
            } else {
                member.updateProfileKeyName(request.profileKeyName());
            }
        }
        String profileUrl = s3Service.generateStaticUrl(member.getProfileKeyName());
        return MemberInfoResponseDto.of(member, profileUrl);
    }

    @Override
    public void acceptTerms(Long memberId, HttpServletResponse response) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.isTermsAccepted()) {
            throw new BadRequestException(ErrorStatus.TERMS_ALREADY_ACCEPTED);
        }
        member.acceptTerms();

        Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(memberId), null, null);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        jwtProvider.writeTokenCookies(response, accessToken, refreshToken);
    }
}
