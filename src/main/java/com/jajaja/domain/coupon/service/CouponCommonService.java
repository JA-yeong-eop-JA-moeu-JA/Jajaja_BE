package com.jajaja.domain.coupon.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.entity.CartProduct;
import com.jajaja.domain.coupon.dto.DiscountResultDto;
import com.jajaja.domain.coupon.entity.Coupon;
import com.jajaja.domain.coupon.entity.enums.ConditionType;
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
    
    /**
     * 쿠폰 적용 가능 여부를 검증합니다.
     */
    public void validateCouponEligibility(Cart cart, Coupon coupon) {
        validateCouponStatus(coupon);
        validateMinOrderAmount(cart, coupon);
        validateCouponConditions(cart, coupon);
        validateCartNotEmpty(cart);
    }

    /**
     * 쿠폰 할인 금액을 계산합니다.
     */
    public DiscountResultDto calculateDiscount(Cart cart, Coupon coupon) {
        if (cart.getCoupon() == null) {
            return DiscountResultDto.noDiscount(cart.calculateTotalAmount());
        }

        int targetAmount = calculateTargetAmount(cart, coupon);
        if (targetAmount <= 0) {
            return DiscountResultDto.noDiscount(cart.calculateTotalAmount());
        }

        int discountAmount = calculateDiscountAmount(targetAmount, coupon);

        log.info("[CouponCommonService] 할인 계산 완료 - 대상금액: {}, 할인금액: {}, 쿠폰: {}", 
                targetAmount, discountAmount, coupon.getName());

        return DiscountResultDto.withDiscount(cart.calculateTotalAmount(), discountAmount);
    }

    private void validateCouponStatus(Coupon coupon) {
        if (coupon.getExpiresAt() != null && coupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CouponHandler(ErrorStatus.COUPON_EXPIRED);
        }
    }

    private void validateMinOrderAmount(Cart cart, Coupon coupon) {
        if (coupon.getMinOrderAmount() != null &&
               cart.calculateTotalAmount() <= coupon.getMinOrderAmount()) {
            throw new CouponHandler(ErrorStatus.COUPON_MIN_ORDER_AMOUNT_NOT_MET);
        }
    }

    private void validateCouponConditions(Cart cart, Coupon coupon) {
        ConditionType conditionType = coupon.getConditionType();
        String conditionValues = coupon.getConditionValues();
        
        switch (conditionType) {
            case ALL:
                // 모든 상품에 적용 가능
                break;
            case BRAND:
                validateBrandCondition(cart, conditionValues);
                break;
            case CATEGORY:
                validateCategoryCondition(cart, conditionValues);
                break;
            default:
                throw new CouponHandler(ErrorStatus.INVALID_COUPON_TYPE);
        }
    }

    private void validateBrandCondition(Cart cart, String brandNames) {
        List<String> allowedBrands = parseConditionValues(brandNames);
        if (allowedBrands.isEmpty()) {
            return;
        }
        
        boolean hasValidBrand = cart.getCartProducts().stream()
                .anyMatch(cartProduct -> {
                    return matchesBrandCondition(cartProduct.getProduct().getStore(), allowedBrands);
                });
        
        if (!hasValidBrand) {
            throw new CouponHandler(ErrorStatus.COUPON_BRAND_CONDITION_NOT_MET);
        }
    }

    private void validateCategoryCondition(Cart cart, String categoryNames) {
        List<String> allowedCategories = parseConditionValues(categoryNames);
        if (allowedCategories.isEmpty()) {
            return;
        }
        
        validateCartNotEmpty(cart);
        
        boolean hasValidCategory = matchesCategoryCondition(couponValidationRepository.findCategoryNamesByCartId(cart.getId()),
                allowedCategories);
        if (!hasValidCategory) {
            throw new CouponHandler(ErrorStatus.COUPON_CATEGORY_CONDITION_NOT_MET);
        }
    }

    private void validateCartNotEmpty(Cart cart) {
        if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
            throw new CouponHandler(ErrorStatus.CART_EMPTY);
        }
    }

    private int calculateTargetAmount(Cart cart, Coupon coupon) {
        return switch (coupon.getConditionType()) {
            case ALL -> cart.calculateTotalAmount();
            case BRAND -> calculateBrandTargetAmount(cart, coupon.getConditionValues());
            case CATEGORY -> calculateCategoryTargetAmount(cart, coupon.getConditionValues());
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
                .mapToInt(CartProduct::getTotalPrice)
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
                .mapToInt(CartProduct::getTotalPrice)
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
}