package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.entity.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSalesRepository extends JpaRepository<ProductSales, Long>, ProductSalesRepositoryCustom {
    List<ProductSales> findByBusinessCategoryIdOrderBySalesCountDesc(Long businessCategoryId);
}
