package com.jajaja.domain.user.service;

import com.jajaja.domain.user.dto.MemberBusinessCategoryRequestDto;
import jakarta.transaction.Transactional;

public interface MemberBusinessCategoryCommandService {

    @Transactional
    void registerUserBusinessCategory(Long userId, MemberBusinessCategoryRequestDto dto);
}
