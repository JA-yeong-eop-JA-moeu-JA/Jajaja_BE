package com.jajaja.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderScheduleService {
    
    private final ApplicationContext applicationContext;
    
    @Async("orderTaskExecutor")
    public void scheduleShippingStart(Long orderId) {
        try {
            log.info("[OrderScheduleService] 30초 배송 대기 시작 - 주문ID: {}", orderId);
            Thread.sleep(30000); // 30초 대기
            log.info("[OrderScheduleService] 30초 대기 완료, 배송 시작 - 주문ID: {}", orderId);
            
            // 순환 참조 방지를 위해 ApplicationContext에서 직접 가져옴
            OrderCommandService orderCommandService = applicationContext.getBean(OrderCommandService.class);
            orderCommandService.startShipping(orderId);
        } catch (InterruptedException e) {
            log.error("[OrderScheduleService] 배송 시작 스케줄링 중단 - 주문ID: {}", orderId, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[OrderScheduleService] 배송 시작 스케줄링 실패 - 주문ID: {}", orderId, e);
        }
    }
}