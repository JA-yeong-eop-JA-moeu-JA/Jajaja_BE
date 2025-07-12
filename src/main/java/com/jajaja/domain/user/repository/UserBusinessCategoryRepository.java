package com.jajaja.domain.user.repository;

import com.jajaja.domain.user.entity.UserBusinessCategory;
import com.jajaja.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBusinessCategoryRepository extends JpaRepository<UserBusinessCategory, Long> {
    Optional<UserBusinessCategory> findByUser(User user);
}
