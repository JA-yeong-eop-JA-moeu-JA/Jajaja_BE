package com.jajaja.domain.coupon.service;

import com.jajaja.domain.coupon.dto.PagingCouponListResponseDto;

public interface CouponQueryService {
	PagingCouponListResponseDto getCouponsByMemberIdWithPaging(Long memberId, int page, int size);
}
