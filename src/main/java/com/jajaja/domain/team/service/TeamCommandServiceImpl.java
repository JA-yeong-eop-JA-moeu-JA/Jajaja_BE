package com.jajaja.domain.team.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.cart.repository.CartRepository;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.domain.user.entity.Member;
import com.jajaja.domain.user.repository.MemberRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandServiceImpl implements TeamCommandService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final TeamRepository teamRepository;
    private final CartRepository cartRepository;
    private final TeamCommonService teamCommonService;

    @Override
    public TeamCreateResponseDto createTeam(Long userId, Long productId) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Team team = Team.builder()
                .leader(member)
                .product(product)
                .status(TeamStatus.MATCHING)
                .expireAt(LocalDateTime.now().plusMinutes(30))
                .build();

        teamRepository.save(team);

        return TeamCreateResponseDto.from(team);
    }

    @Override
    public void joinTeam(Long userId, Long teamId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.TEAM_NOT_FOUND));

        teamCommonService.joinTeam(member, team);
    }

    @Override
    public void joinTeamInCarts(Long userId, Long productId) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));

        List<Team> matchingTeams = teamRepository.findMatchingTeamsByProductId(productId);

        if (matchingTeams.isEmpty()) {
            throw new BadRequestException(ErrorStatus.TEAM_NOT_FOUND);
        }

        // 가장 유효 시간에 임박한 팀 선택
        Team team = matchingTeams.get(0);

        teamCommonService.joinTeam(member, team);

        // 장바구니에서 해당 product 삭제
        Cart cart = member.getCart();
        cart.deleteAllCartProductsByProductId(productId);

        // TODO: 주문 생성으로 넘어가는 로직 작성 필요
    }

}
