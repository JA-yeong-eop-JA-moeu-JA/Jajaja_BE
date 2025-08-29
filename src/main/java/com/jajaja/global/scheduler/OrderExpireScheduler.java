package com.jajaja.global.scheduler;

import com.jajaja.domain.order.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpireScheduler {

	private final OrderCommandService orderCommandService;
	
	@Scheduled(fixedDelay = 60000)
	public void expireOrders() {
		log.info("[OrderExpireScheduler] 만료된 주문 처리 스케줄러 시작");
		orderCommandService.expireOrder();
		log.info("[OrderExpireScheduler] 만료된 주문 처리 스케줄러 완료");
	}
}
