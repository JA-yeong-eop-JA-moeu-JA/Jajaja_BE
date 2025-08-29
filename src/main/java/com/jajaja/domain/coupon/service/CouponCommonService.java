package com.jajaja.domain.coupon.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.order.repository.OrderRepository;
import com.jajaja.global.common.dto.PriceInfoDto;
import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.DiscountType;
import com.jajaja.domain.coupon.repository.CouponValidationRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.handler.CouponHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCommonService {

    private static final String CONDITION_DELIMITER = ",";
    
    private final CouponValidationRepository couponValidationRepository;
    private final OrderRepository orderRepository;
    
    /**
     * 장바구니 전체에 대한 쿠폰 적용 가능 여부를 boolean으로 반환합니다.
     * */
    public boolean checkCouponEligibility(Cart cart, Coupon coupon) {
        Member member = (cart != null) ? cart.getMember() : null;
        List<CartProduct> products = (cart != null) ? cart.getCartProducts() : Collections.emptyList();
        int totalAmount = (cart != null) ? cart.calculateTotalAmount() : 0;

        return getCouponInvalidityReason(member, products, totalAmount, coupon).isEmpty();
    }

    /**
     * 선택된 아이템들을 대상으로 쿠폰 적용 가능 여부를 검증하고, 실패 시 예외를 발생시킵니다.
     */
    public void validateCouponForSelectedItems(List<CartProduct> selectedItems, Coupon coupon) {
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new CouponHandler(ErrorStatus.CART_EMPTY);
        }
        Member member = selectedItems.get(0).getCart().getMember();
        int totalAmount = selectedItems.stream().mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity()).sum();

        getCouponInvalidityReason(member, selectedItems, totalAmount, coupon)
                .ifPresent(errorStatus -> { throw new CouponHandler(errorStatus); });
    }

    /**
     * 쿠폰 할인 금액을 계산합니다.
     */
    public PriceInfoDto calculateDiscount(Cart cart, Coupon coupon) {
        if (cart.getCoupon() == null) {
            return PriceInfoDto.noDiscount(cart.calculateTotalAmount());
        }

        int targetAmount = calculateTargetAmount(cart, coupon);
        if (targetAmount <= 0) {
            return PriceInfoDto.noDiscount(cart.calculateTotalAmount());
        }

        int discountAmount = calculateDiscountAmount(targetAmount, coupon);

        log.info("[CouponCommonService] 할인 계산 완료 - 대상금액: {}, 할인금액: {}, 쿠폰: {}", 
                targetAmount, discountAmount, coupon.getName());

        return PriceInfoDto.withDiscount(cart.calculateTotalAmount(), discountAmount);
    }
    
    /**
     * 선택된 장바구니 상품들에 대한 쿠폰 할인 금액을 계산합니다.
     */
    public PriceInfoDto calculateDiscountForSelectedItems(List<CartProduct> selectedItems, Coupon coupon) {
        int originalAmount = selectedItems.stream()
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
                
        int targetAmount = calculateTargetAmountForSelectedItems(selectedItems, coupon);
        if (targetAmount <= 0) {
            return PriceInfoDto.noDiscount(originalAmount);
        }

        int discountAmount = calculateDiscountAmount(targetAmount, coupon);

        log.info("[CouponCommonService] 선택된 상품 할인 계산 완료 - 원래금액: {}, 대상금액: {}, 할인금액: {}, 쿠폰: {}", 
                originalAmount, targetAmount, discountAmount, coupon.getName());

        return PriceInfoDto.withDiscount(originalAmount, discountAmount);
    }

    private int calculateTargetAmount(Cart cart, Coupon coupon) {
        return switch (coupon.getConditionType()) {
            case ALL, FIRST -> cart.calculateTotalAmount();
            case BRAND -> calculateBrandTargetAmount(cart, coupon.getConditionValues());
            case CATEGORY -> calculateCategoryTargetAmount(cart, coupon.getConditionValues());
		};
    }
    
    private int calculateTargetAmountForSelectedItems(List<CartProduct> selectedItems, Coupon coupon) {
        return switch (coupon.getConditionType()) {
            case ALL, FIRST -> selectedItems.stream()
                    .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                    .sum();
            case BRAND -> calculateBrandTargetAmountForItems(selectedItems, coupon.getConditionValues());
            case CATEGORY -> calculateCategoryTargetAmountForItems(selectedItems, coupon.getConditionValues());
        };
    }

    private int calculateDiscountAmount(int targetAmount, Coupon coupon) {
        DiscountType discountType = coupon.getDiscountType();
        Integer discountValue = coupon.getDiscountValue();
        
        if (discountValue == null || discountValue <= 0) {
            return 0;
        }

        int discountAmount;
        
        switch (discountType) {
            case PERCENTAGE:
                discountAmount = Math.round(targetAmount * discountValue / 100.0f);
                break;
                
            case FIXED_AMOUNT:
                discountAmount = discountValue;
                break;
                
            default:
                log.warn("[CouponCommonService] 알 수 없는 할인 타입: {}", discountType);
                return 0;
        }

        return Math.min(discountAmount, targetAmount);
    }

    // 브랜드에 해당하는 제품들의 금액에서만 할인
    private int calculateBrandTargetAmount(Cart cart, String brandNames) {
        List<String> allowedBrands = parseConditionValues(brandNames);
        if (allowedBrands.isEmpty()) {
            return 0;
        }
        
        return cart.getCartProducts().stream()
                .filter(cartProduct -> matchesBrandCondition(cartProduct.getProduct().getStore(), allowedBrands))
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
    }
    
    private int calculateBrandTargetAmountForItems(List<CartProduct> selectedItems, String brandNames) {
        List<String> allowedBrands = parseConditionValues(brandNames);
        if (allowedBrands.isEmpty()) {
            return 0;
        }
        
        return selectedItems.stream()
                .filter(cartProduct -> matchesBrandCondition(cartProduct.getProduct().getStore(), allowedBrands))
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
    }

    // 카테고리에 해당하는 제품들의 금액에서만 할인
    private int calculateCategoryTargetAmount(Cart cart, String categoryNames) {
        List<String> allowedCategories = parseConditionValues(categoryNames);
        if (allowedCategories.isEmpty()) {
            return 0;
        }
        
        Map<Long, List<String>> productCategoryMap = getProductCategoryMap(cart);
        
        return cart.getCartProducts().stream()
                .filter(cartProduct -> {
                    List<String> productCategories = productCategoryMap.getOrDefault(
                            cartProduct.getProduct().getId(), Collections.emptyList());
                    return matchesCategoryCondition(productCategories, allowedCategories);
                })
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
    }
    
    private int calculateCategoryTargetAmountForItems(List<CartProduct> selectedItems, String categoryNames) {
        List<String> allowedCategories = parseConditionValues(categoryNames);
        if (allowedCategories.isEmpty()) {
            return 0;
        }
        
        List<Long> productIds = selectedItems.stream()
                .map(cp -> cp.getProduct().getId())
                .toList();
        
        Map<Long, List<String>> productCategoryMap = new HashMap<>();
        List<Object[]> categoryResults = couponValidationRepository.findCategoryNamesByProductIds(productIds);
        
        for (Object[] result : categoryResults) {
            Long productId = (Long) result[0];
            String categoryName = (String) result[1];
            productCategoryMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(categoryName);
        }
        
        return selectedItems.stream()
                .filter(cartProduct -> {
                    List<String> productCategories = productCategoryMap.getOrDefault(
                            cartProduct.getProduct().getId(), Collections.emptyList());
                    return matchesCategoryCondition(productCategories, allowedCategories);
                })
                .mapToInt(cp -> cp.getUnitPrice() * cp.getQuantity())
                .sum();
    }

    private boolean matchesBrandCondition(String productStore, List<String> allowedBrands) {
        if (productStore == null || allowedBrands.isEmpty()) {
            return false;
        }
        
        return allowedBrands.stream()
                .anyMatch(brand -> productStore.trim().equalsIgnoreCase(brand.trim()));
    }

    private boolean matchesCategoryCondition(List<String> productCategories, List<String> allowedCategories) {
        if (productCategories.isEmpty() || allowedCategories.isEmpty()) {
            return false;
        }
        
        return allowedCategories.stream()
                .anyMatch(allowedCategory -> 
                    productCategories.stream()
                        .anyMatch(productCategory -> 
                            productCategory.trim().equalsIgnoreCase(allowedCategory.trim())
                        )
                );
    }

    private List<String> parseConditionValues(String conditionValues) {
        if (conditionValues == null || conditionValues.trim().isEmpty()) {
            return List.of();
        }
        
        return Arrays.stream(conditionValues.split(CONDITION_DELIMITER))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private Map<Long, List<String>> getProductCategoryMap(Cart cart) {
        List<Long> productIds = cart.getCartProducts().stream()
                .map(cp -> cp.getProduct().getId())
                .toList();
        
        Map<Long, List<String>> productCategoryMap = new HashMap<>();
        List<Object[]> categoryResults = couponValidationRepository.findCategoryNamesByProductIds(productIds);
        
        for (Object[] result : categoryResults) {
            Long productId = (Long) result[0];
            String categoryName = (String) result[1];
            productCategoryMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(categoryName);
        }
        
        return productCategoryMap;
    }

    /**
     * 쿠폰 검증에 실패하면 실패 원인(ErrorStatus)을 담은 Optional을, 성공하면 빈 Optional을 반환합니다.
     */
    private Optional<ErrorStatus> getCouponInvalidityReason(Member member, List<CartProduct> products, int totalAmount, Coupon coupon) {
        // 쿠폰 만료일 기준 만료 여부 검증
        if (coupon.getExpiresAt() != null && coupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.of(ErrorStatus.COUPON_EXPIRED);
        }

        // 최소 주문 금액 검증
        if (coupon.getMinOrderAmount() != null && totalAmount < coupon.getMinOrderAmount()) {
            return Optional.of(ErrorStatus.COUPON_MIN_ORDER_AMOUNT_NOT_MET);
        }

        // 쿠폰 세부 조건 검증
        switch (coupon.getConditionType()) {
            case ALL:
                if (products.isEmpty()) return Optional.of(ErrorStatus.CART_EMPTY);
                break;

            case FIRST:
                if (member == null) return Optional.of(ErrorStatus.MEMBER_NOT_FOUND);
                if (orderRepository.existsByMember(member)) return Optional.of(ErrorStatus.COUPON_NOT_AVAILABLE);
                break;

            case BRAND:
                if (products.isEmpty()) return Optional.of(ErrorStatus.CART_EMPTY);
                List<String> allowedBrands = parseConditionValues(coupon.getConditionValues());
                if (!allowedBrands.isEmpty()) {
                    boolean hasValidBrand = products.stream()
                            .anyMatch(cp -> matchesBrandCondition(cp.getProduct().getStore(), allowedBrands));
                    if (!hasValidBrand) return Optional.of(ErrorStatus.COUPON_BRAND_CONDITION_NOT_MET);
                }
                break;

            case CATEGORY:
                if (products.isEmpty()) return Optional.of(ErrorStatus.CART_EMPTY);
                List<String> allowedCategories = parseConditionValues(coupon.getConditionValues());
                if (!allowedCategories.isEmpty()) {
                    List<Long> productIds = products.stream().map(cp -> cp.getProduct().getId()).toList();
                    List<String> productCategoryNames = couponValidationRepository.findCategoryNamesByProductIds(productIds)
                            .stream()
                            .map(result -> (String) result[1])
                            .distinct()
                            .toList();
                    if (!matchesCategoryCondition(productCategoryNames, allowedCategories)) {
                        return Optional.of(ErrorStatus.COUPON_CATEGORY_CONDITION_NOT_MET);
                    }
                }
                break;

            default:
                return Optional.of(ErrorStatus.INVALID_COUPON_TYPE);
        }
        return Optional.empty(); // 모든 검증 통과
    }
}