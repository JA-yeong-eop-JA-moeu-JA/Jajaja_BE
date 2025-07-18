package com.jajaja.domain.product.dto.projection;

import com.jajaja.domain.product.entity.Product;

public record ProductTotalSalesDto(Product product, Long totalSales) {
    public ProductTotalSalesDto(Product product, Integer totalSales) {
        this(product, totalSales != null ? totalSales.longValue() : 0L);
    }
}
