package com.jajaja.domain.delivery.service;

import com.jajaja.domain.delivery.dto.DeliveryRequestDto;
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
	public void addDeliveryAddress(Long memberId, DeliveryRequestDto request) {
		Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new DeliveryHandler(ErrorStatus.MEMBER_NOT_FOUND)
		);
		
		if(request.isDefault()) {
			updateDefaultAddress(member);
		}
		
		deliveryRepository.save(Delivery.create(
				request.name(),
				request.phone(),
				request.address(),
				request.addressDetail(),
				request.zipcode(),
				request.buildingPassword(),
				request.isDefault(),
				member
		));
	}
	
	@Override
	public void deleteDeliveryAddress(Long memberId, Long deliveryId) {
		Member member = memberRepository.findById(memberId).orElseThrow( () -> new DeliveryHandler(ErrorStatus.MEMBER_NOT_FOUND));
		deliveryRepository.delete(getOwnDelivery(member, deliveryId));
	}
	
	@Override
	public void updateDeliveryAddress(Long memberId, Long deliveryId, DeliveryRequestDto request) {
		Member member = memberRepository.findById(memberId).orElseThrow( () -> new DeliveryHandler(ErrorStatus.MEMBER_NOT_FOUND));
		Delivery delivery = getOwnDelivery(member, deliveryId);
		
		if(request.isDefault()) {
			updateDefaultAddress(member);
		}
		delivery.update(
				request.name(),
				request.phone(),
				request.address(),
				request.addressDetail(),
				request.zipcode(),
				request.buildingPassword(),
				request.isDefault()
		);
	}
	
	private void updateDefaultAddress(Member  member) {
		deliveryRepository.findByMemberAndIsDefaultIsTrue(member)
				.ifPresent(Delivery::removeDefault);
	}
	
	/**
	 * 멤버 아이디와 배송지 아이디를 기반으로 중복되는 에러 처리를 메소드로 분리하였습니다.
	 * @param member	멤버 엔티티
	 * @param deliveryId	배송지 아이디
	 */
	private Delivery getOwnDelivery(Member member, Long deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new DeliveryHandler(ErrorStatus.DELIVERY_NOT_FOUND));
		
		//  배송지 데이터베이스에 저장된 멤버와 현재 로그인 된 멤버가 다를 경우
		if(delivery.getMember() != member) {
			throw new DeliveryHandler(ErrorStatus.DELIVERY_MEMBER_NOT_MATCH);
		}
		
		return delivery;
	}
}