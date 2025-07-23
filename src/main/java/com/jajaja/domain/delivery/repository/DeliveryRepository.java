package com.jajaja.domain.delivery.repository;

import com.jajaja.domain.delivery.entity.Delivery;
import com.jajaja.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	List<Delivery> findAllByMember(Member member);
	Optional<Delivery> findByMemberAndIsDefaultIsTrue(Member member);
}
