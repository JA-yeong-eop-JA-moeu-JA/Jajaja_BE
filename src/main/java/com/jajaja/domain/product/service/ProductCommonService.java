package com.jajaja.domain.product.service;

import com.jajaja.domain.review.entity.Review;
import java.util.List;

public interface ProductCommonService {
    double calculateAverageRating(List<Review> reviews);
}
