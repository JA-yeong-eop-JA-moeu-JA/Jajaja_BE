package com.jajaja.domain.search.dto;

import com.jajaja.domain.product.dto.response.ProductListResponseDto;
import com.jajaja.global.apiPayload.PageResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagingSearchProductListResponseDto(
        PageResponse page,
        List<ProductListResponseDto> products
) {
    public static PagingSearchProductListResponseDto of(Page<?> page, List<ProductListResponseDto> productDtos) {
        return PagingSearchProductListResponseDto.builder()
                .page(PageResponse.from(page))
                .products(productDtos)
                .build();
    }
}
