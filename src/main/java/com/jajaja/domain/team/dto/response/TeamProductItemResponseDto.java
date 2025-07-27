package com.jajaja.domain.team.dto.response;

import com.jajaja.domain.member.entity.Member;
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
    public static TeamProductItemResponseDto from(Team team) {
        Product product = team.getProduct();
        Member leader = team.getLeader();

        return TeamProductItemResponseDto.builder()
                .teamId(team.getId())
                .nickname(team.getLeader().getName())
                .leaderProfileImageUrl(leader.getProfileUrl())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .discountRate(product.getDiscountRate())
                .thumbnailUrl(product.getThumbnailUrl())
                .build();
    }
}