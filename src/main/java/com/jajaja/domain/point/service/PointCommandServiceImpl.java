package com.jajaja.domain.point.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.order.repository.OrderProductRepository;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.point.repository.PointRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointCommandServiceImpl implements PointCommandService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public void usePoints(Long memberId, int amountToUse, long orderProductId) {
        List<Point> points = pointRepository.findValidReviewPointsOrderedByOldest(memberId, LocalDate.now());
        int remainingToUse = amountToUse;
        for (Point point : points) {
            int canUse = point.getAvailableAmount();
            if (canUse == 0) continue;
            int useNow = Math.min(remainingToUse, canUse);
            point.use(useNow);
            remainingToUse -= useNow;
            if (remainingToUse <= 0) break;
        }
        if (remainingToUse > 0) {
            throw new BadRequestException(ErrorStatus.INSUFFICIENT_POINT);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_PRODUCT_NOT_FOUND));
        Point usedPoint = Point.builder()
                .type(PointType.USE)
                .amount(amountToUse)
                .member(member)
                .orderProduct(orderProduct)
                .build();
        pointRepository.saveAll(points);
        pointRepository.save(usedPoint);
    }

    @Override
    public void addReviewPoints(Long memberId, int amount, long orderProductId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_PRODUCT_NOT_FOUND));
        Point point = Point.builder()
                .type(PointType.REVIEW)
                .amount(amount)
                .usedAmount(0)
                // 30일 후 만료로 설정
                .expiresAt(LocalDate.now().plusDays(30))
                .member(member)
                .orderProduct(orderProduct)
                .build();
        pointRepository.save(point);
    }

    @Override
    public void cancelReviewPoint(Long orderProductId) {
        Point reviewPoint = pointRepository.findReviewPointByOrderProductId(orderProductId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.POINT_NOT_FOUND));
        boolean alreadyCancelled = pointRepository.existsByTypeAndOrderProduct(PointType.CANCEL, reviewPoint.getOrderProduct());
        if (alreadyCancelled) {
            throw new BadRequestException(ErrorStatus.ALREADY_CANCELLED_POINT);
        }
        Point cancelPoint = Point.builder()
                .type(PointType.CANCEL)
                .amount(reviewPoint.getAmount())
                .member(reviewPoint.getMember())
                .orderProduct(reviewPoint.getOrderProduct())
                .build();
        pointRepository.save(cancelPoint);
    }

    @Override
    public void refundUsedPoints(Long orderProductId) {
        Point usePoint = pointRepository.findUsePointByOrderProductId(orderProductId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.POINT_NOT_FOUND));
        boolean alreadyRefunded = pointRepository.existsByTypeAndOrderProduct(PointType.REFUND, usePoint.getOrderProduct());
        if (alreadyRefunded) {
            throw new BadRequestException(ErrorStatus.ALREADY_REFUNDED_POINT);
        }
        Point refundPoint = Point.builder()
                .type(PointType.REFUND)
                .amount(usePoint.getAmount())
                .member(usePoint.getMember())
                .orderProduct(usePoint.getOrderProduct())
                .expiresAt(usePoint.getExpiresAt())
                .usedAmount(0)
                .build();
        pointRepository.save(refundPoint);
    }
}
