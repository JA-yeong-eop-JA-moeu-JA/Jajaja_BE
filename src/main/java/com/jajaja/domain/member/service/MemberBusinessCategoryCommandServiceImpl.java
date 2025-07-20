package com.jajaja.domain.member.service;

import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.repository.BusinessCategoryRepository;
import com.jajaja.domain.member.dto.MemberBusinessCategoryRequestDto;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberBusinessCategory;
import com.jajaja.domain.member.repository.MemberBusinessCategoryRepository;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jajaja.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class MemberBusinessCategoryCommandServiceImpl implements MemberBusinessCategoryCommandService {

    private final MemberRepository memberRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final MemberBusinessCategoryRepository memberBusinessCategoryRepository;

    /**
     * 유저 업종 등록
     * @param userId 유저 아이디
     * @param dto 업종 요청 DTO
     */
    @Transactional
    public void registerUserBusinessCategory(Long userId, MemberBusinessCategoryRequestDto dto) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (memberBusinessCategoryRepository.findByMember(member).isPresent()) {
            throw new GeneralException(ErrorStatus.BUSINESS_CATEGORY_ALREADY_REGISTERED);
        }

        BusinessCategory businessCategory = businessCategoryRepository.findById(dto.businessCategoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUSINESS_CATEGORY_NOT_FOUND));

        MemberBusinessCategory memberBusinessCategory = MemberBusinessCategory.builder()
                .member(member)
                .businessCategory(businessCategory)
                .build();

        memberBusinessCategoryRepository.save(memberBusinessCategory);
    }
}
