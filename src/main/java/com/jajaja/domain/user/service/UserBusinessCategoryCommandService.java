package com.jajaja.domain.user.service;

import com.jajaja.domain.user.dto.UserBusinessCategoryRequestDto;
import jakarta.transaction.Transactional;

public interface UserBusinessCategoryCommandService {

    @Transactional
    void registerUserBusinessCategory(Long userId, UserBusinessCategoryRequestDto dto);
}
