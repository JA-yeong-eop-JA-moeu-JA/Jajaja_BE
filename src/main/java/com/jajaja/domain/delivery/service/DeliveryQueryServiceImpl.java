package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryResponseDto;
import com.jajaja.domain.delivery.repository.DeliveryRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.DeliveryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryQueryServiceImpl  implements DeliveryQueryService {
	
	private final DeliveryRepository deliveryRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public List<DeliveryResponseDto> getDeliveriesByMemberId(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new DeliveryHandler(ErrorStatus.MEMBER_NOT_FOUND)
		);
		
		return deliveryRepository.findAllByMember(member).stream()
				.map(DeliveryResponseDto::of)
				.collect(Collectors.toList());
	}
}
