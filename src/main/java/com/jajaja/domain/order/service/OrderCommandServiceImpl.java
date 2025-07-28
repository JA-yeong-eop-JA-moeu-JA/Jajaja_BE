package com.jajaja.domain.order.service;

import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.cart.repository.CartProductRepository;
import com.jajaja.domain.cart.service.CartCommandService;
import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.service.CouponCommonService;
import com.jajaja.domain.delivery.entity.Delivery;
import com.jajaja.domain.delivery.repository.DeliveryRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.entity.MemberCoupon;
import com.jajaja.domain.member.repository.MemberCouponRepository;
import com.jajaja.domain.member.repository.MemberRepository;
import com.jajaja.domain.order.dto.request.OrderCreateRequestDto;
import com.jajaja.domain.order.dto.request.OrderPrepareRequestDto;
import com.jajaja.domain.order.dto.request.OrderRefundRequestDto;
import com.jajaja.domain.order.dto.response.OrderCreateResponseDto;
import com.jajaja.domain.order.dto.response.OrderPrepareResponseDto;
import com.jajaja.domain.order.dto.response.OrderRefundResponseDto;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.entity.OrderProduct;
import com.jajaja.domain.order.entity.enums.OrderStatus;
import com.jajaja.domain.order.entity.enums.OrderType;
import com.jajaja.domain.order.repository.OrderRepository;
import com.jajaja.domain.point.entity.Point;
import com.jajaja.domain.point.repository.PointRepository;
import com.jajaja.domain.point.service.PointQueryService;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final CartProductRepository cartProductRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final PointRepository pointRepository;
    private final CartCommandService cartCommandService;
    private final CouponCommonService couponCommonService;
    private final PointQueryService pointQueryService;
    private final IamportClient iamportClient;

    @Override
    public OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request) {
        log.info("[OrderCommandService] 결제 준비 시작 - 회원ID: {}", memberId);

        Member member = findMember(memberId);
        Delivery delivery = findDelivery(request.getAddressId(), memberId);
        List<CartProduct> cartProducts = findAndValidateCartProducts(request.getItems(), memberId);
        Coupon coupon = validateCoupon(request.getAppliedCouponId(), memberId, cartProducts);
        if (request.getPoint() != null && request.getPoint() > 0) {
            validatePointUsage(request.getPoint(), memberId);
        }

        int totalAmount = cartProducts.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();

        int couponDiscount = calculateCouponDiscount(coupon, totalAmount);
        int shippingFee = calculateShippingFee();
        int finalAmount = totalAmount - couponDiscount - (request.getPoint() != null ? request.getPoint() : 0) + shippingFee;

        String merchantUid = "ORD-" + UUID.randomUUID();

        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .orderType(OrderType.PERSONAL)
                .discountAmount(couponDiscount)
                .pointUsedAmount(request.getPoint() != null ? request.getPoint() : 0)
                .shippingFee(shippingFee)
                .orderNumber(merchantUid)
                .totalAmount(totalAmount)
                .paidAmount(finalAmount)
                .member(member)
                .delivery(delivery)
                .coupon(coupon)
                .build();

        cartProducts.forEach(cartProduct -> {
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .product(cartProduct.getProduct())
                    .productOption(cartProduct.getProductOption())
                    .quantity(cartProduct.getQuantity())
                    .price(cartProduct.getUnitPrice())
                    .status(OrderStatus.PENDING)
                    .build();
            order.getOrderProducts().add(orderProduct);
        });

        orderRepository.save(order);

        log.info("[OrderCommandService] 결제 준비 완료 - 회원ID: {}, merchant_uid: {}, 최종금액: {}", memberId, merchantUid, finalAmount);

        return OrderPrepareResponseDto.of(
                merchantUid,
                totalAmount,
                couponDiscount,
                request.getPoint() != null ? request.getPoint() : 0,
                shippingFee,
                finalAmount
        );
    }

    @Override
    public OrderCreateResponseDto createOrder(Long memberId, OrderCreateRequestDto request) {
        log.info("[OrderCommandService] 주문 생성 시작 - 회원ID: {}, 아임포트UID: {}", memberId, request.getImpUid());

        Order order = orderRepository.findByMerchantUid(request.getMerchantUid())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_NOT_FOUND));

        verifyPayment(request.getImpUid(), order);

        try {
            order.updatePaymentInfo(request.getImpUid(), request.getPaymentMethod(), OrderStatus.PAYMENT_COMPLETED);

            if (order.getPointUsedAmount() > 0) {
                usePoints(memberId, order.getPointUsedAmount());
            }

            if (order.getCoupon() != null) {
                useCoupon(memberId, order.getCoupon());
            }

            cartCommandService.deleteCartProducts(memberId, request.getItems());

            log.info("[OrderCommandService] 주문 생성 완료 - 주문ID: {}", order.getId());

            return OrderCreateResponseDto.of(order);

        } catch (Exception e) {
            log.error("[OrderCommandService] 주문 생성 실패 - 회원ID: {}, 에러: {}", memberId, e.getMessage(), e);
            order.updateStatus(OrderStatus.PAYMENT_FAILED);
            processIamportRefund(order, "주문 생성 실패로 인한 자동 환불");
            throw e;
        }
    }

    private void verifyPayment(String impUid, Order order) {
        try {
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);

            if (paymentResponse.getResponse() == null) {
                throw new BadRequestException(ErrorStatus.PAYMENT_NOT_FOUND);
            }

            Payment payment = paymentResponse.getResponse();

            if (!"paid".equals(payment.getStatus())) {
                order.updateStatus(OrderStatus.PAYMENT_FAILED);
                throw new BadRequestException(ErrorStatus.PAYMENT_NOT_COMPLETED);
            }

            if (!payment.getMerchantUid().equals(order.getMerchantUid())) {
                order.updateStatus(OrderStatus.CANCELLED);
                processIamportRefund(order, "결제 정보 불일치로 인한 자동 환불");
                throw new BadRequestException(ErrorStatus.PAYMENT_MERCHANT_UID_MISMATCH);
            }

            if (payment.getAmount().intValue() != order.getPaidAmount()) {
                order.updateStatus(OrderStatus.CANCELLED);
                processIamportRefund(order, "결제 금액 불일치로 인한 자동 환불");
                throw new BadRequestException(ErrorStatus.PAYMENT_AMOUNT_MISMATCH);
            }

            log.info("[OrderCommandService] 결제 검증 완료 - impUid: {}, merchantUid: {}, 금액: {}",
                    impUid, order.getMerchantUid(), payment.getAmount());

        } catch (IamportResponseException | IOException e) {
            log.error("[OrderCommandService] 결제 검증 실패 - impUid: {}, merchantUid: {}", impUid, order.getMerchantUid(), e);
            throw new BadRequestException(ErrorStatus.PAYMENT_VERIFICATION_FAILED);
        }
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private Delivery findDelivery(Long deliveryId, Long memberId) {
        return deliveryRepository.findById(deliveryId)
                .filter(delivery -> delivery.getMember().getId().equals(memberId))
                .orElseThrow(() -> new BadRequestException(ErrorStatus.DELIVERY_NOT_FOUND));
    }

    private List<CartProduct> findAndValidateCartProducts(List<Long> cartProductIds, Long memberId) {
        List<CartProduct> cartProducts = cartProductRepository.findAllById(cartProductIds);

        if (cartProducts.size() != cartProductIds.size()) {
            throw new BadRequestException(ErrorStatus.CART_PRODUCT_NOT_FOUND);
        }

        cartProducts.forEach(cartProduct -> {
            if (!cartProduct.getCart().getMember().getId().equals(memberId)) {
                throw new BadRequestException(ErrorStatus.CART_PRODUCT_NOT_FOUND);
            }
        });

        return cartProducts;
    }

    private Coupon validateCoupon(Long couponId, Long memberId, List<CartProduct> cartProducts) {
        if (couponId == null) {
            return null;
        }

        MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponIdAndUsedAtIsNull(memberId, couponId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.COUPON_NOT_AVAILABLE));

        Coupon coupon = memberCoupon.getCoupon();

        couponCommonService.validateCouponForSelectedItems(cartProducts, coupon);
        log.info("[OrderCommandService] 쿠폰 검증 완료 - 쿠폰ID: {}, 회원ID: {}", couponId, memberId);

        return coupon;
    }

    private void validatePointUsage(Integer pointToUse, Long memberId) {
        if (pointToUse == null || pointToUse <= 0) {
            return;
        }
        int availablePoints = pointQueryService.getPointBalance(memberId).balance();
        if (availablePoints < pointToUse) {
            throw new BadRequestException(ErrorStatus.INSUFFICIENT_POINT);
        }
    }

    private int calculateCouponDiscount(Coupon coupon, int totalAmount) {
        if (coupon == null) {
            return 0;
        }

        Integer discountValue = coupon.getDiscountValue();
        if (discountValue == null || discountValue <= 0) {
            return 0;
        }

        int discountAmount;
        switch (coupon.getDiscountType()) {
            case PERCENTAGE:
                discountAmount = Math.round(totalAmount * discountValue / 100.0f);
                break;
            case FIXED_AMOUNT:
                discountAmount = discountValue;
                break;
            default:
                return 0;
        }
        return Math.min(discountAmount, totalAmount);
    }

    private int calculateShippingFee() {
        // TODO : 배송비 로직 구현
        return 3000;
    }

    private void usePoints(Long memberId, int pointToUse) {
        List<Point> availablePoints = pointRepository.findAvailablePointsByMemberIdOrderByCreatedAtAsc(memberId);

        int remainingToUse = pointToUse;

        for (Point point : availablePoints) {
            if (remainingToUse <= 0) break;

            int availableAmount = point.getAvailableAmount();
            int useAmount = Math.min(remainingToUse, availableAmount);

            point.use(useAmount);
            remainingToUse -= useAmount;
        }
    }

    private void useCoupon(Long memberId, Coupon coupon) {
        MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponIdAndUsedAtIsNull(memberId, coupon.getId())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.COUPON_NOT_AVAILABLE));

        memberCoupon.use();
    }

    @Override
    public OrderRefundResponseDto refundOrder(Long memberId, OrderRefundRequestDto request) {
        log.info("[OrderCommandService] 환불 처리 시작 - 회원ID: {}, 주문ID: {}", memberId, request.getOrderId());

        Order order = findOrderForRefund(request.getOrderId(), memberId);
        validateRefundable(order);

        order.updateStatus(OrderStatus.REFUND_REQUESTED);

        try {
            processIamportRefund(order, request.getRefundReason());

            if (order.getPointUsedAmount() > 0) {
                refundPoints(order);
            }

            order.updateStatus(OrderStatus.REFUNDED);
            log.info("[OrderCommandService] 환불 처리 완료 - 주문ID: {}", order.getId());

            return OrderRefundResponseDto.of(order, order.getPointUsedAmount(), request.getRefundReason());

        } catch (Exception e) {
            order.updateStatus(OrderStatus.REFUND_FAILED);
            log.error("[OrderCommandService] 환불 처리 실패 - 주문ID: {}, 에러: {}", request.getOrderId(), e.getMessage(), e);
            throw new BadRequestException(ErrorStatus.REFUND_FAILED);
        }
    }

    private void refundPoints(Order order) {
        int remainingToRefund = order.getPointUsedAmount();
        if (remainingToRefund <= 0) {
            return;
        }

        List<Point> usedPoints = pointRepository.findByMemberIdAndUsedAmountGreaterThanOrderByCreatedAtDesc(order.getMember().getId(), 0);

        for (Point point : usedPoints) {
            if (remainingToRefund <= 0) break;

            int currentUsed = point.getUsedAmount();
            int refundAmount = Math.min(remainingToRefund, currentUsed);

            point.decreaseUsedAmount(refundAmount);
            remainingToRefund -= refundAmount;
        }

        if (remainingToRefund > 0) {
            log.warn("[OrderCommandService] 환불해야 할 포인트가 남았지만, 사용된 포인트를 찾을 수 없습니다. 회원ID: {}, 남은 포인트: {}",
                    order.getMember().getId(), remainingToRefund);
        }
        log.info("[OrderCommandService] 포인트 환불 완료 - 회원ID: {}, 환불포인트: {}",
                order.getMember().getId(), order.getPointUsedAmount());
    }

    private Order findOrderForRefund(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(memberId)) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_FOUND);
        }

        return order;
    }

    private void validateRefundable(Order order) {
        if (order.getOrderStatus() == OrderStatus.REFUNDED || order.getOrderStatus() == OrderStatus.REFUND_REQUESTED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_REFUNDED);
        }

        if (order.getOrderStatus() != OrderStatus.PAYMENT_COMPLETED && order.getOrderStatus() != OrderStatus.PREPARING) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_REFUNDABLE);
        }
    }

    private void processIamportRefund(Order order, String refundReason) {
        try {
            CancelData cancelData =
                    new com.siot.IamportRestClient.request.CancelData(order.getImpUid(), true);
            cancelData.setReason(refundReason);
            iamportClient.cancelPaymentByImpUid(cancelData);

        } catch (Exception e) {
            log.error("[OrderCommandService] 아임포트 환불 실패 - impUid: {}", order.getImpUid(), e);
            throw new BadRequestException(ErrorStatus.REFUND_FAILED);
        }
    }
}
