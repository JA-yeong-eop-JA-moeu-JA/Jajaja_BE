package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.team.entity.Team;
import lombok.Builder;

@Builder
public record TeamProductItemResponseDto(
        Long teamId,
        String nickname,
        String leaderProfileImageUrl,
        Long productId,
        String productName,
        Integer price,
        Integer discountRate,
        String thumbnailUrl
) {
    public static TeamProductItemResponseDto of(Team team, String profileUrl) {
        Product product = team.getProduct();

        return TeamProductItemResponseDto.builder()
                .teamId(team.getId())
                .nickname(team.getLeader().getName())
                .leaderProfileImageUrl(profileUrl)
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .discountRate(product.getDiscountRate())
                .thumbnailUrl(product.getThumbnailUrl())
                .build();
    }
}