package com.jajaja.domain.user.repository;

import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.entity.enums.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByOauthIdAndOauthType(String oauthId, OauthType oauthType);
}
