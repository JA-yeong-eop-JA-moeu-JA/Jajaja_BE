package com.jajaja.domain.search.service;

import com.jajaja.domain.product.converter.ProductConverter;
import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductSalesRepository;
import com.jajaja.domain.search.dto.PopularSearchKeywordsResponseDto;
import com.jajaja.domain.search.entity.Search;
import com.jajaja.domain.search.entity.enums.SearchSort;
import com.jajaja.domain.search.repository.SearchRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;
    private final ProductConverter productConverter;
    private final ProductSalesRepository productSalesRepository;

    /**
     * 키워드 기반 상품 검색
     *
     * @param keyword 검색어
     * @param sort 정렬 기준 (POPULAR, NEW, LOW_PRICE, REVIEW)
     * @return 상품 리스트 DTO
     */
    @Override
    @Transactional
    public List<ProductListResponseDto> searchProductsByKeyword(String keyword, SearchSort sort) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException(ErrorStatus.INVALID_KEYWORD);
        }

        saveOrIncrementKeyword(keyword);

        List<Product> products = searchRepository.findProductsByKeyword(keyword);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        sortProducts(products, sort);

        return products.stream()
                .map(productConverter::toProductListResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 인기 검색어 Top 8 조회
     *
     * @return 인기 검색어 DTO
     */
    @Override
    public PopularSearchKeywordsResponseDto getPopularKeywords() {
        var pageable = PageRequest.of(0, 8);
        List<String> keywords = searchRepository.findTopSearchKeywords(pageable);
        return PopularSearchKeywordsResponseDto.of(keywords);
    }

    @Transactional
    protected void saveOrIncrementKeyword(String keyword) {
        searchRepository.findByName(keyword)
                .ifPresentOrElse(
                        Search::increaseCount,
                        () -> searchRepository.save(Search.builder()
                                .name(keyword)
                                .searchCount(1)
                                .build())
                );
    }

    private void sortProducts(List<Product> products, SearchSort sort) {
        switch (sort) {
            case NEW:
                sortByNew(products);
                break;
            case LOW_PRICE:
                sortByLowPrice(products);
                break;
            case REVIEW:
                sortByReview(products);
                break;
            case POPULAR:
            default:
                sortByTotalSales(products);
                break;
        }
    }

    private void sortByNew(List<Product> products) {
        products.sort(Comparator.comparing(Product::getCreatedAt).reversed());
    }

    private void sortByLowPrice(List<Product> products) {
        products.sort(Comparator.comparing(Product::getPrice));
    }

    private void sortByReview(List<Product> products) {
        products.sort(Comparator.comparingInt(p -> p.getReviews().size()));
        Collections.reverse(products);
    }

    private void sortByTotalSales(List<Product> products) {
        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        // QueryDSL로 상품별 판매량 조회
        Map<Long, Long> salesMap = productSalesRepository.findTotalSalesByProductIds(productIds);

        // 판매량 내림차순 정렬
        products.sort((p1, p2) -> salesMap.getOrDefault(p2.getId(), 0L)
                .compareTo(salesMap.getOrDefault(p1.getId(), 0L)));
    }
}
