package com.jajaja.domain.user.service;

import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.entity.repository.BusinessCategoryRepository;
import com.jajaja.domain.user.dto.UserBusinessCategoryRequestDto;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.entity.UserBusinessCategory;
import com.jajaja.domain.user.repository.UserBusinessCategoryRepository;
import com.jajaja.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserBusinessCategoryService {

    private final UserRepository userRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final UserBusinessCategoryRepository userBusinessCategoryRepository;

    public UserBusinessCategoryService(
            UserRepository userRepository,
            BusinessCategoryRepository businessCategoryRepository,
            UserBusinessCategoryRepository userBusinessCategoryRepository
    ) {
        this.userRepository = userRepository;
        this.businessCategoryRepository = businessCategoryRepository;
        this.userBusinessCategoryRepository = userBusinessCategoryRepository;
    }

    /**
     * 유저 업종 등록
     * @param userId 유저 아이디
     * @param dto 업종 요청 DTO
     */
    @Transactional
    public void registerUserBusinessCategory(Long userId, UserBusinessCategoryRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다."));

        BusinessCategory businessCategory = businessCategoryRepository.findById(dto.businessCategoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 업종을 찾을 수 없습니다."));

        UserBusinessCategory userBusinessCategory = userBusinessCategoryRepository.findByUser(user)
                .orElseGet(() -> UserBusinessCategory.builder()
                        .user(user)
                        .businessCategory(businessCategory)
                        .build());

        userBusinessCategory.setBusinessCategory(businessCategory);
        userBusinessCategoryRepository.save(userBusinessCategory);
    }
}
