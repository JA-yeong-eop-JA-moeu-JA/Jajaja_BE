package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findBySubCategoryOrderByCreatedAtDesc(Long subcategoryId, Pageable pageable);
    List<Product> findBySubCategoryOrderByPriceAsc(Long subcategoryId, Pageable pageable);
    List<Product> findBySubCategoryOrderByReviewCountDesc(Long subcategoryId, Pageable pageable);
}

