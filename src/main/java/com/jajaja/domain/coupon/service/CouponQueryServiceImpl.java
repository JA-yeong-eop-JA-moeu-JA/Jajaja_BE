package com.jajaja.domain.coupon.service;

import com.jajaja.domain.coupon.dto.CouponResponseDto;
import com.jajaja.domain.coupon.dto.PagingCouponListResponseDto;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.domain.member.repository.MemberCouponRepository;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CouponHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService{
	
	private final MemberRepository memberRepository;
	private final MemberCouponRepository memberCouponRepository;
	
	@Override
	public PagingCouponListResponseDto getCouponsByMemberIdWithPaging(Long memberId, int page, int size) {
		memberRepository.findById(memberId).orElseThrow(() -> new CouponHandler(ErrorStatus.MEMBER_NOT_FOUND));
		
		Pageable pageable = PageRequest.of(page, size);
		Page<MemberCoupon> memberCouponPage = memberCouponRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
		
		List<CouponResponseDto> couponDtos = memberCouponPage.getContent().stream()
				.map(memberCoupon -> CouponResponseDto.from(memberCoupon.getCoupon()))
				.toList();
		
		return PagingCouponListResponseDto.of(memberCouponPage, couponDtos);
	}
}
