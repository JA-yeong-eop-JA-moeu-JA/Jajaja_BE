package com.jajaja.domain.product.repository;

import com.jajaja.domain.product.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {
}

