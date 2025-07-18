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
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
