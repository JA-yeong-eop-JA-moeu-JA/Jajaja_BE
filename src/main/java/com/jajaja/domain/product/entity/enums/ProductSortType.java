package com.jajaja.domain.product.entity.enums;

import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSortType {
    POPULAR, NEW, PRICE_ASC, REVIEW;

    public static ProductSortType from(String value) {
        try {
            return ProductSortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ErrorStatus.INVALID_PRODUCT_SORT_TYPE);
        }
    }
}