package com.jajaja.domain.product.service;

import com.jajaja.domain.review.entity.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCommonServiceImpl implements ProductCommonService {

    @Override
    public double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return Math.round(
                reviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0) * 10.0
        ) / 10.0;
    }

    @Override
    public Integer calculateDiscountedPrice(Integer price, Integer discountRate) {
        if (discountRate == null || discountRate == 0) {
            return price;
        }
        double discountMultiplier = 1 - (discountRate / 100.0);
        return Integer.valueOf((int) Math.round(price * discountMultiplier));
    }
}
