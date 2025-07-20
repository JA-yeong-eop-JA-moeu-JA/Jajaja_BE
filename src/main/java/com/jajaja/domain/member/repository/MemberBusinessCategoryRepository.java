package com.jajaja.domain.member.repository;

import com.jajaja.domain.member.entity.MemberBusinessCategory;
import com.jajaja.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberBusinessCategoryRepository extends JpaRepository<MemberBusinessCategory, Long> {
    Optional<MemberBusinessCategory> findByMember(Member member);
}
