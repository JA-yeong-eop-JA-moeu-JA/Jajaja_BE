package com.jajaja.domain.product.service;

import com.jajaja.domain.product.dto.response.ProductOptionResponseDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.product.util.ProductPriceCalculator;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionQueryServiceImpl implements ProductOptionQueryService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductOptionResponseDto> getProductOptions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        return product.getProductOptions().stream()
                .map(option -> ProductOptionResponseDto.of(
                        option.getId(),
                        option.getName(),
                        option.getPrice(),
                        ProductPriceCalculator.calculateDiscountedPrice(
                                option.getPrice(),
                                product.getDiscountRate()
                        )
                ))
                .toList();
    }
}
