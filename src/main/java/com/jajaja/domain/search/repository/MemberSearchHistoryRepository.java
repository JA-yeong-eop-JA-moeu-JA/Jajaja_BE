package com.jajaja.domain.search.repository;

import com.jajaja.domain.search.entity.MemberSearchHistory;
import com.jajaja.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberSearchHistoryRepository extends JpaRepository<MemberSearchHistory, Long> {
    List<MemberSearchHistory> findTop10ByMemberOrderByCreatedAtDesc(Member member);
    Optional<MemberSearchHistory> findByMemberAndKeyword(Member member, String keyword);
    List<MemberSearchHistory> findByMemberOrderByCreatedAtDesc(Member member);
    Optional<MemberSearchHistory> findByMemberAndId(Member member, Long id);
}
