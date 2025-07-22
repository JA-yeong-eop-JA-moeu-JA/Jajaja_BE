package com.jajaja.domain.search.repository;

import com.jajaja.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchRepositoryCustom {
    List<Product> findProductsByKeyword(String keyword);
    List<String> findTopSearchKeywords(Pageable pageable);
}
