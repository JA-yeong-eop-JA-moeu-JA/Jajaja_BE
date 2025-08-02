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
import com.jajaja.domain.product.entity.ProductSales;
import com.jajaja.domain.product.repository.ProductSalesRepository;
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
    
    private final IamportClient iamportClient;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final CartProductRepository cartProductRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final ProductSalesRepository productSalesRepository;
    private final CartCommandService cartCommandService;
    private final CouponCommonService couponCommonService;
    
    @Override
    public OrderPrepareResponseDto prepareOrder(Long memberId, OrderPrepareRequestDto request) {
        log.info("[OrderCommandService] 결제 준비 시작 - 회원ID: {}", memberId);

        Member member = findMember(memberId);
        Delivery delivery = deliveryRepository.findById(request.getAddressId())
                .filter(del -> del.getMember().getId().equals(memberId))
                .orElseThrow(() -> new BadRequestException(ErrorStatus.DELIVERY_NOT_FOUND));
        List<CartProduct> cartProducts = findAndValidateCartProducts(request.getItems(), memberId);
        Coupon coupon = validateCoupon(request.getAppliedCouponId(), memberId, cartProducts);
       validatePointUsage(request.getPoint(), member);
       
        int totalAmount = cartProducts.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();

        int couponDiscount = calculateCouponDiscount(coupon, totalAmount);
        int shippingFee = calculateShippingFee(delivery);
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

        Member member = findMember(memberId);
        Order order = orderRepository.findByMerchantUid(request.getMerchantUid())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_NOT_FOUND));

        verifyPayment(request.getImpUid(), order);

        try {
            order.updatePaymentInfo(request.getImpUid(), request.getPaymentMethod(), OrderStatus.PAYMENT_COMPLETED);
            
            validatePointUsage(request.getPoint(), member);
            member.updatePoint(member.getPoint() - order.getPointUsedAmount()); // TODO : 포인트 사용 내역 저장

            if (order.getCoupon() != null) {
                memberCouponRepository.findByMemberIdAndCouponIdAndUsedAtIsNull(memberId, order.getCoupon().getId())
                        .orElseThrow(() -> new BadRequestException(ErrorStatus.COUPON_NOT_AVAILABLE)).use();
            }

            cartCommandService.deleteCartProducts(memberId, request.getItems());
            
            // 판매 완료 시 판매 카운트 증가
            order.getOrderProducts()
                    .forEach((pr) -> {
                        if(productSalesRepository.updateSalesByProductIdAndBusinessCategory(pr.getProduct(),
                                member.getMemberBusinessCategory().getBusinessCategory()
                                , pr.getQuantity()) <= 0) {
                            ProductSales newProductSales = ProductSales.builder()
                                    .product(pr.getProduct())
                                    .businessCategory(member.getMemberBusinessCategory().getBusinessCategory())
                                    .salesCount(pr.getQuantity())
                                    .build();
                            productSalesRepository.save(newProductSales);
                        }
                    });
            
            log.info("[OrderCommandService] 주문 생성 완료 - 주문ID: {}", order.getId());
            
            return OrderCreateResponseDto.of(order);
            
        } catch (Exception e) {
            log.error("[OrderCommandService] 주문 생성 실패 - 회원ID: {}, 에러: {}", memberId, e.getMessage(), e);
            order.updateStatus(OrderStatus.PAYMENT_FAILED);
            processIamportRefund(order, "주문 생성 실패로 인한 자동 환불");
            throw e;
        }
    }

    @Override
    public OrderRefundResponseDto refundOrder(Long memberId, OrderRefundRequestDto request) {
        log.info("[OrderCommandService] 환불 처리 시작 - 회원ID: {}, 주문ID: {}", memberId, request.getOrderId());
        
        Member member = findMember(memberId);
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_NOT_FOUND));
        
        if (!order.getMember().equals(member)) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_FOUND);
        }
        
        // 환불이 가능한지 확인
        if (order.getOrderStatus() == OrderStatus.REFUNDED || order.getOrderStatus() == OrderStatus.REFUND_REQUESTED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_REFUNDED);
        }
        if (order.getOrderStatus() != OrderStatus.PAYMENT_COMPLETED && order.getOrderStatus() != OrderStatus.PREPARING) {
            throw new BadRequestException(ErrorStatus.ORDER_NOT_REFUNDABLE);
        }
        
        order.updateStatus(OrderStatus.REFUND_REQUESTED);

        try {
            processIamportRefund(order, request.getRefundReason());

            if (order.getPointUsedAmount() > 0) {
                member.updatePoint(member.getPoint() + order.getPointUsedAmount());
            }

            order.updateStatus(OrderStatus.REFUNDED);
            
            // 환불 성공 시 구매 통계에서 판매 개수만큼 수량 줄이기
            order.getOrderProducts()
                    .forEach((pr) -> {
                        long updatedRows = productSalesRepository.updateSalesByProductIdAndBusinessCategory(pr.getProduct(),
                                member.getMemberBusinessCategory().getBusinessCategory(),
                                (pr.getQuantity() * -1));
                        
                        if(updatedRows <= 0) { // 만약 업데이트가 안 됐다면 로깅
                            log.warn("OrderCommandService] 환불 후 판매량 차감 실패 - 상품ID: {}, 비즈니스카테고리ID: {}, 주문ID: {}", pr.getId(), member.getMemberBusinessCategory().getBusinessCategory().getId(), order.getId());
                        }
                    });
            
            log.info("[OrderCommandService] 환불 처리 완료 - 주문ID: {}", order.getId());

            return OrderRefundResponseDto.of(order, order.getPointUsedAmount(), request.getRefundReason());

        } catch (Exception e) {
            order.updateStatus(OrderStatus.REFUND_FAILED);
            log.error("[OrderCommandService] 환불 처리 실패 - 주문ID: {}, 에러: {}", request.getOrderId(), e.getMessage(), e);
            throw new BadRequestException(ErrorStatus.REFUND_FAILED);
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
    
    private void validatePointUsage(Integer pointToUse, Member member) {
        if (pointToUse == null || pointToUse <= 0) {
            return;
        }
        
        if (member.getPoint() < pointToUse) {
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
    
    private int calculateShippingFee(Delivery delivery) {
        if (delivery.getZipcode() == null || delivery.getZipcode().isEmpty()) {
            throw new BadRequestException(ErrorStatus.DELIVERY_ZIPCODE_NOT_FOUND);
        }
        
        int zipcode = Integer.parseInt(delivery.getZipcode());
        // 제주 및 도서산간 지역 우편번호 범위
        if ((zipcode >= 63002 && zipcode <= 63644) ||
                (zipcode >= 63000 && zipcode <= 63001) ||
                zipcode == 15654 ||
                (zipcode >= 23008 && zipcode <= 23010) ||
                (zipcode >= 23100 && zipcode <= 23116) ||
                (zipcode >= 23124 && zipcode <= 23136) ||
                zipcode == 32133 ||
                zipcode == 33411 ||
                (zipcode >= 40200 && zipcode <= 40240) ||
                (zipcode >= 52570 && zipcode <= 52571) ||
                (zipcode >= 53031 && zipcode <= 53033) ||
                (zipcode >= 53088 && zipcode <= 53104)) {
            return 3000;
        }
        return 0;
    }

    private void processIamportRefund(Order order, String refundReason) {
        try {
            CancelData cancelData =
                    new CancelData(order.getImpUid(), true, java.math.BigDecimal.valueOf(Math.max(0, order.getPaidAmount() - 6000)));
            cancelData.setReason(refundReason);
            iamportClient.cancelPaymentByImpUid(cancelData);

        } catch (Exception e) {
            log.error("[OrderCommandService] 아임포트 환불 실패 - impUid: {}", order.getImpUid(), e);
            throw new BadRequestException(ErrorStatus.REFUND_FAILED);
        }
    }
}
