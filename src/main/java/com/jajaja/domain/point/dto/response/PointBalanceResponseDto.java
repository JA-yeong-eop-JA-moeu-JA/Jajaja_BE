package com.jajaja.domain.point.dto.response;

public record PointBalanceResponseDto(
        int balance
) {
    public static PointBalanceResponseDto from(int balance) {
        return new PointBalanceResponseDto(balance);
    }
}
