package com.jajaja.domain.user.repository;

import com.jajaja.domain.user.entity.MemberBusinessCategory;
import com.jajaja.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberBusinessCategoryRepository extends JpaRepository<MemberBusinessCategory, Long> {
    Optional<MemberBusinessCategory> findByMember(Member member);
}
