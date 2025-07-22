package com.jajaja.domain.search.repository;

import com.jajaja.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {
    Optional<Search> findByName(String name);
}
