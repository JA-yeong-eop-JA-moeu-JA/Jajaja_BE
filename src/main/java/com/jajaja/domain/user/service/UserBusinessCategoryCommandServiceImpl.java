package com.jajaja.domain.user.service;

import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.repository.BusinessCategoryRepository;
import com.jajaja.domain.user.dto.UserBusinessCategoryRequestDto;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.entity.UserBusinessCategory;
import com.jajaja.domain.user.repository.UserBusinessCategoryRepository;
import com.jajaja.domain.user.repository.UserRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jajaja.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class UserBusinessCategoryCommandServiceImpl implements UserBusinessCategoryCommandService {

    private final UserRepository userRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final UserBusinessCategoryRepository userBusinessCategoryRepository;

    /**
     * 유저 업종 등록
     * @param userId 유저 아이디
     * @param dto 업종 요청 DTO
     */
    @Transactional
    public void registerUserBusinessCategory(Long userId, UserBusinessCategoryRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (userBusinessCategoryRepository.findByUser(user).isPresent()) {
            throw new GeneralException(ErrorStatus.BUSINESS_CATEGORY_ALREADY_REGISTERED);
        }

        BusinessCategory businessCategory = businessCategoryRepository.findById(dto.businessCategoryId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUSINESS_CATEGORY_NOT_FOUND));

        UserBusinessCategory userBusinessCategory = UserBusinessCategory.builder()
                .user(user)
                .businessCategory(businessCategory)
                .build();

        userBusinessCategoryRepository.save(userBusinessCategory);
    }
}
