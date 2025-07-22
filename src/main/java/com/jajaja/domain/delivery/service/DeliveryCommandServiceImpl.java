package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryAddRequestDto;
import com.jajaja.domain.delivery.entity.Delivery;
import com.jajaja.domain.delivery.repository.DeliveryRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.DeliveryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryCommandServiceImpl implements DeliveryCommandService{
	
	private final DeliveryRepository deliveryRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public void addDeliveryAddress(Long memberId, DeliveryAddRequestDto request) {
		Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new DeliveryHandler(ErrorStatus.MEMBER_NOT_FOUND)
		);
		
		if(request.isDefault()) {
			if(deliveryRepository.findByMemberAndIsDefaultIsTrue(member).isPresent()) {
				throw new DeliveryHandler(ErrorStatus.DELIVERY_ALREADY_DEFAULT);
			}
		}
		
		deliveryRepository.save(Delivery.builder()
				.name(request.name())
				.phone(request.phone())
				.address(request.address())
				.addressDetail(request.addressDetail())
				.zipcode(request.zipcode())
				.buildingPassword(request.buildingPassword())
				.isDefault(request.isDefault())
				.member(member)
				.build());
	}
}
