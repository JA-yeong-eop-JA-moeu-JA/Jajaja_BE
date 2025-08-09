package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.dto.projection.ProductTotalSalesDto;
import com.jajaja.domain.product.entity.BusinessCategory;
import com.jajaja.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductSalesRepositoryCustom {
    List<ProductTotalSalesDto> findTopProductsByTotalSales(Pageable pageable);
    Map<Long, Long> findTotalSalesByProductIds(List<Long> productIds);
    List<Product> findProductsBySubCategoryOrderBySalesDesc(Long subcategoryId, Pageable pageable);
    long updateSalesByProductIdAndBusinessCategory(Product product, BusinessCategory businessCategory, int count);
}
