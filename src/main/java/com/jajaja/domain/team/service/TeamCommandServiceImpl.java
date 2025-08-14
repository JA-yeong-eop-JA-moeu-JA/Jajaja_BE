package com.jajaja.domain.team.service;

import com.jajaja.domain.cart.entity.Cart;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.member.repository.MemberRepository;
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
    private final TeamCommonServiceImpl teamCommonService;

    @Override
    public TeamCreateResponseDto createTeam(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

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
    public void joinTeam(Long memberId, Long teamId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));

        Team team = teamRepository.findByIdWithLeaderMembersAndProduct(teamId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.TEAM_NOT_FOUND));

        Member leader = team.getLeader();

        teamCommonService.joinTeam(member, leader, team);
    }

    @Override
    public void joinTeamInCarts(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Team> matchingTeams = teamRepository.findMatchingTeamsByProductId(productId);

        if (matchingTeams.isEmpty()) {
            throw new BadRequestException(ErrorStatus.TEAM_NOT_FOUND);
        }

        // 가장 유효 시간에 임박한 팀 선택
        Team team = matchingTeams.get(0);

        Member leader = team.getLeader();

        teamCommonService.joinTeam(member, leader, team);

        // 장바구니에서 해당 product 삭제
        Cart cart = member.getCart();
        cart.deleteAllCartProductsByProductId(productId);
    }

}
