package com.jajaja.domain.search.repository;

import com.jajaja.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchRepositoryCustom {
    List<String> findTopSearchKeywords(Pageable pageable);
    Page<Product> findProductsByKeywordWithPaging(String keyword, int page, int size);
}
