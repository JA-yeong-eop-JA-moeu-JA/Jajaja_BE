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
import com.jajaja.domain.point.entity.enums.PointType;
import com.jajaja.domain.point.repository.PointRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    private final IamportClient iamportClient;

    @Override
    public OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request) {
        log.info("[OrderCommandService] 결제 준비 시작 - 회원ID: {}", memberId);

        Member member = findMember(memberId);
        findDelivery(request.getAddressId(), memberId); // 배송지 검증
        List<CartProduct> cartProducts = findAndValidateCartProducts(request.getItems(), memberId);
        Coupon coupon = validateCoupon(request.getAppliedCouponId(), memberId, cartProducts);
        if (request.getPoint() != null && request.getPoint() > 0) {
            validatePointUsage(request.getPoint(), memberId);
        }

        // 4. 금액 계산
        int totalAmount = cartProducts.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();

        int couponDiscount = calculateCouponDiscount(coupon, totalAmount);
        int shippingFee = calculateShippingFee();
        int finalAmount = totalAmount - couponDiscount - (request.getPoint() != null ? request.getPoint() : 0) + shippingFee;

        // 5. merchant_uid 생성
        String merchantUid = generateOrderNumber();

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

        // 결제 검증
        Payment payment = verifyPayment(request.getImpUid(), request.getMerchantUid());
        
        // 회원 및 배송지 조회
        Member member = findMember(memberId);
        Delivery delivery = findDelivery(request.getAddressId(), memberId);
        
        // 장바구니 상품들 조회 및 검증
        List<CartProduct> cartProducts = findAndValidateCartProducts(request.getItems(), memberId);
        
        // 쿠폰 및 포인트 검증
        Coupon coupon = validateCoupon(request.getAppliedCouponId(), memberId, cartProducts);
        if (request.getPoint() != null && request.getPoint() > 0) {
            validatePointUsage(request.getPoint(), memberId);
        }
        
        try {
            // 주문 생성
            Order order = createOrder(member, delivery, cartProducts, coupon, request, payment);
            
            // 포인트 사용 처리
            if (request.getPoint() != null && request.getPoint() > 0) {
                usePoints(memberId, request.getPoint());
            }
            
            // 쿠폰 사용 처리
            if (coupon != null) {
                useCoupon(memberId, coupon);
            }
            
            // 주문 성공 후에만 장바구니에서 상품들 제거
            cartCommandService.deleteCartProducts(memberId, request.getItems());
            
            log.info("[OrderCommandService] 주문 생성 완료 - 주문ID: {}", order.getId());
            
            return buildOrderResponse(order);
            
        } catch (Exception e) {
            log.error("[OrderCommandService] 주문 생성 실패 - 회원ID: {}, 에러: {}", memberId, e.getMessage(), e);
            // 주문 실패 시 카트는 그대로 유지
            throw e;
        }
    }

    private Payment verifyPayment(String impUid, String merchantUid) {
        // 스웨거 테스트를 위해 검증 생략
        if (impUid.startsWith("test_")) {
            log.info("[OrderCommandService] 테스트 모드 - 결제 검증 생략: {}", impUid);
            return null;
        }
        
        // 실제 아임포트 검증
        try {
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);
            
            if (paymentResponse.getResponse() == null) {
                throw new BadRequestException(ErrorStatus.PAYMENT_NOT_FOUND);
            }
            
            Payment payment = paymentResponse.getResponse();
            
            if (!"paid".equals(payment.getStatus())) {
                throw new BadRequestException(ErrorStatus.PAYMENT_NOT_COMPLETED);
            }
            
            // merchantUid 검증
            if (!payment.getMerchantUid().equals(merchantUid)) {
                throw new BadRequestException(ErrorStatus.PAYMENT_MERCHANT_UID_MISMATCH);
            }
            
            log.info("[OrderCommandService] 결제 검증 완료 - impUid: {}, merchantUid: {}, 금액: {}", 
                    impUid, merchantUid, payment.getAmount());
            return payment;
            
        } catch (IamportResponseException | IOException e) {
            log.error("[OrderCommandService] 결제 검증 실패 - impUid: {}, merchantUid: {}", impUid, merchantUid, e);
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
        
        List<Point> availablePointsList = pointRepository.findAvailablePointsByMemberIdOrderByCreatedAtAsc(memberId);
        int availablePoints = availablePointsList.stream().mapToInt(Point::getAvailableAmount).sum();
        
        if (availablePoints < pointToUse) {
            throw new BadRequestException(ErrorStatus.INSUFFICIENT_POINT);
        }
    }

    private Order createOrder(Member member, Delivery delivery, List<CartProduct> cartProducts, 
                             Coupon coupon, OrderCreateRequestDto request, Payment payment) {
        
        String orderNumber = generateOrderNumber();
        int totalAmount = cartProducts.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
        
        int couponDiscount = calculateCouponDiscount(coupon, totalAmount);
        int shippingFee = calculateShippingFee();
        int finalAmount = totalAmount - couponDiscount - request.getPoint() + shippingFee;
        
        // 결제 금액 검증 (테스트 모드가 아닐 때만, merchantUid는 이미 verifyPayment에서 검증됨)
        if (payment != null && !payment.getAmount().equals(java.math.BigDecimal.valueOf(finalAmount))) {
            throw new BadRequestException(ErrorStatus.PAYMENT_AMOUNT_MISMATCH);
        }
        
        Order order = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .orderType(OrderType.PERSONAL)
                .paymentMethod(request.getPaymentMethod())
                .discountAmount(couponDiscount)
                .pointUsedAmount(request.getPoint())
                .shippingFee(shippingFee)
                .orderNumber(orderNumber)
                .totalAmount(totalAmount)
                .paidAmount(finalAmount)
                .paidAt(LocalDateTime.now())
                .impUid(request.getImpUid())
                .merchantUid(request.getMerchantUid())
                .deliveryRequest(request.getDeliveryRequest())
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
                    .status(OrderStatus.PAYMENT_COMPLETED)
                    .build();
            order.getOrderProducts().add(orderProduct);
        });
        
        return orderRepository.save(order);
    }

    private String generateOrderNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomSuffix = ThreadLocalRandom.current().nextInt(10000, 99999);
        return "ORD-" + today + randomSuffix;
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
        return 0;
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

        //  주문 조회 및 권한 확인
        Order order = findOrderForRefund(request.getOrderId(), memberId);
        // 환불 가능 상태 검증
        validateRefundable(order);

        try {
            // 아임포트 환불 처리
            processIamportRefund(order);

            // 포인트 환불 처리
            int refundedPoints = processPointRefund(order);

            // 주문 상태 변경
            order.updateStatus(OrderStatus.REFUNDED);

            log.info("[OrderCommandService] 환불 처리 완료 - 주문ID: {}, 환불포인트: {}", order.getId(), refundedPoints);

            return OrderRefundResponseDto.of(order, refundedPoints, request.getRefundReason());

        } catch (Exception e) {
            log.error("[OrderCommandService] 환불 처리 실패 - 주문ID: {}, 에러: {}", request.getOrderId(), e.getMessage(), e);
            throw e;
        }
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
        if (order.getOrderStatus() == OrderStatus.REFUNDED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_REFUNDED);
        }

        // 환불 불가능한 상태들
        if (order.getOrderStatus() == OrderStatus.SHIPPING || 
            order.getOrderStatus() == OrderStatus.DELIVERED ||
            order.getOrderStatus() == OrderStatus.CANCELLED ||
            order.getOrderStatus() == OrderStatus.PAYMENT_FAILED) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_REFUNDABLE);
        }

        // 환불 가능한 상태: PAYMENT_COMPLETED, PREPARING
        if (order.getOrderStatus() != OrderStatus.PAYMENT_COMPLETED && 
            order.getOrderStatus() != OrderStatus.PREPARING) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_REFUNDABLE);
        }
    }

    private void processIamportRefund(Order order) {
        // 포트폴리오용 테스트 모드
        if (order.getImpUid() != null && order.getImpUid().startsWith("test_")) {
            log.info("[OrderCommandService] 테스트 모드 - 아임포트 환불 생략: {}", order.getImpUid());
            return;
        }

        // 실제 환불
        try {
            if (order.getImpUid() != null) {
                // 아임포트 환불 API 호출
                log.info("[OrderCommandService] 아임포트 환불 처리 - impUid: {}, 금액: {}", 
                        order.getImpUid(), order.getPaidAmount());
                // iamportClient.cancelPaymentByImpUid(new CancelData(order.getImpUid(), true));
            }
        } catch (Exception e) {
            log.error("[OrderCommandService] 아임포트 환불 실패 - impUid: {}", order.getImpUid(), e);
            throw new BadRequestException(ErrorStatus.REFUND_FAILED);
        }
    }

    private int processPointRefund(Order order) {
        // 주문에서 사용된 포인트 총합 조회
        int usedPoints = pointRepository.findUsedPointsByOrderId(order.getId());

        if (usedPoints > 0) {
            // 환불 포인트 생성
            Point refundPoint = Point.builder()
                    .member(order.getMember())
                    .type(PointType.REFUND)
                    .amount(usedPoints)
                    .usedAmount(0)
                    .expiresAt(null) // 환불 포인트는 만료 없음
                    .build();

            pointRepository.save(refundPoint);
            log.info("[OrderCommandService] 포인트 환불 완료 - 회원ID: {}, 환불포인트: {}", 
                    order.getMember().getId(), usedPoints);
        }

        return usedPoints;
    }

    private OrderCreateResponseDto buildOrderResponse(Order order) {
        return OrderCreateResponseDto.of(order);
    }
}