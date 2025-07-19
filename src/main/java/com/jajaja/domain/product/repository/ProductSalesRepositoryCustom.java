package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.projection.ProductTotalSalesDto;
import com.jajaja.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductSalesRepositoryCustom {
    List<ProductTotalSalesDto> findTopProductsByTotalSales(Pageable pageable);
    List<Product> findProductsBySubCategoryOrderBySalesDesc(Long subcategoryId, Pageable pageable);
}
