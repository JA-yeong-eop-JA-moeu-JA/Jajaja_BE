package com.jajaja.domain.member.service;

import com.jajaja.domain.member.dto.MemberBusinessCategoryRequestDto;
import jakarta.transaction.Transactional;

public interface MemberBusinessCategoryCommandService {

    @Transactional
    void registerUserBusinessCategory(Long userId, MemberBusinessCategoryRequestDto dto);
}
