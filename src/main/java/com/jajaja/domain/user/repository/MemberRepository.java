package com.jajaja.domain.user.repository;

import com.jajaja.domain.user.entity.Member;
import com.jajaja.domain.user.entity.enums.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByOauthIdAndOauthType(String oauthId, OauthType oauthType);
}
