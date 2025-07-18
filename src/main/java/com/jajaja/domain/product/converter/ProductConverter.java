package com.jajaja.domain.product.converter;

import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.service.ProductCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final ProductCommonService productCommonService;

    public ProductListResponseDto toProductListResponseDto(Product product) {
        double rating = productCommonService.calculateAverageRating(product.getReviews());
        int reviewCount = product.getReviews().size();
        return ProductListResponseDto.of(product, rating, reviewCount);
    }
}
