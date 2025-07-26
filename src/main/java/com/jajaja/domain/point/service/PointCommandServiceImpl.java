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

    /**
     * 포인트를 사용합니다. 사용 가능한 리뷰 포인트를 찾아서 순차적으로 사용합니다.
     * @param memberId 회원 ID
     * @param amountToUse 사용하려는 포인트 양
     * @param orderProductId 주문 상품 ID
     */
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

    /**
     * 리뷰 작성 후 포인트를 추가합니다.
     * @param memberId 회원 ID
     * @param amount 포인트 양
     * @param orderProductId 주문 상품 ID
     */
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

    /**
     * 포인트 획득을 취소합니다.
     * @param orderProductId 주문 상품 ID
     */
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

    /**
     * 사용된 포인트를 환불합니다.
     * @param orderProductId 주문 상품 ID
     */
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
