package com.jajaja.domain.product.util;

public class ProductPriceCalculator {
    public static int calculateDiscountedPrice(int price, Integer discountRate) {
        if (discountRate == null || discountRate == 0) {
            return price;
        }
        double discountMultiplier = 1 - (discountRate / 100.0);
        return (int) Math.round(price * discountMultiplier);
    }
}
