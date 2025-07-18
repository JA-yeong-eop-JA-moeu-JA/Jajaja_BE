package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.entity.ProductSales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {

    List<ProductSales> findByBusinessCategoryIdOrderBySalesCountDesc(Long businessCategoryId);

    @Query("""
        select ps.product as product, sum(ps.salesCount) as totalSales
        from ProductSales ps
        group by ps.product
        order by totalSales desc
        """)
    List<ProductTotalSalesProjection> findTopProductsByTotalSales(Pageable pageable);

    interface ProductTotalSalesProjection {
        Product getProduct();
    }
}
