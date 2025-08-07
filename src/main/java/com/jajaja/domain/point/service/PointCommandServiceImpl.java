package com.jajaja.domain.point.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.point.repository.PointRepository;
import com.jajaja.domain.review.entity.Review;
import com.jajaja.domain.review.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;

    /**
     * 포인트를 사용합니다. 사용 가능한 리뷰 포인트를 찾아서 순차적으로 사용합니다.
     *
     * @param memberId 회원 ID
     * @param order    주문
     */
    @Override
    public void usePoints(Long memberId, Order order) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Point> points = pointRepository.findValidPointsOrderedByOldest(memberId, LocalDate.now());
        int remainingToUse = order.getPointUsedAmount();
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
        Point usedPoint = Point.builder()
                .type(PointType.USE)
                .amount(order.getPointUsedAmount())
                .member(member)
                .order(order)
                .build();
        pointRepository.saveAll(points);
        pointRepository.save(usedPoint);
    }

    /**
     * 리뷰 작성 후 포인트를 추가합니다.
     *
     * @param memberId 회원 ID
     * @param reviewId 리뷰 ID
     */
    @Override
    public void addReviewPoints(Long memberId, long reviewId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.REVIEW_NOT_FOUND));
        Point point = Point.builder()
                .type(PointType.REVIEW)
                .amount(100) // 고정 100 포인트 지급
                .usedAmount(0)
                // 30일 후 만료로 설정
                .expiresAt(LocalDate.now().plusDays(30))
                .member(member)
                .review(review)
                .build();
        pointRepository.save(point);
    }

    /**
     * 사용된 포인트를 환불합니다.
     *
     * @param orderId 주문 ID
     */
    @Override
    public void refundUsedPoints(Long orderId) {
        Point usePoint = pointRepository.findUsePointByOrderId(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.POINT_NOT_FOUND));
        if (pointRepository.existsByTypeAndOrder(PointType.REFUND, usePoint.getOrder())) {
            throw new BadRequestException(ErrorStatus.ALREADY_REFUNDED_POINT);
        }

        // 사용된 포인트의 usedAmount를 복구
        Order order = usePoint.getOrder();
        int totalToRefund = usePoint.getAmount();
        for (Point earnedPoint : order.getPoints()) {
            if (!earnedPoint.getType().isEarnType()) continue;
            int used = earnedPoint.getUsedAmount();
            if (used == 0) continue;
            int refundAmount = Math.min(totalToRefund, used);
            earnedPoint.restoreUsedAmount(refundAmount);
            totalToRefund -= refundAmount;
            if (totalToRefund == 0) break;
        }

        Point refundPoint = Point.builder()
                .type(PointType.REFUND)
                .amount(usePoint.getAmount())
                .member(usePoint.getMember())
                .order(usePoint.getOrder())
                .build();
        pointRepository.save(refundPoint);
    }

    @Override
    public void addFirstPurchasePointsIfPossible(Member member) {
        if (!pointRepository.existsByMemberAndType(member, PointType.FIRST_PURCHASE)) {
            Point point = Point.builder()
                    .type(PointType.FIRST_PURCHASE)
                    .amount(500) // 첫 구매 포인트 500 지급
                    .usedAmount(0)
                    // 30일 후 만료로 설정
                    .expiresAt(LocalDate.now().plusDays(30))
                    .member(member)
                    .build();
            pointRepository.save(point);
        }
    }
}