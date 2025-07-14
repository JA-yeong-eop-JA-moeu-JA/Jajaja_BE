package com.jajaja.domain.team.service;

import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.product.repository.ProductRepository;
import com.jajaja.domain.team.dto.response.TeamCreateResponseDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.TeamMember;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.domain.user.entity.User;
import com.jajaja.domain.user.repository.UserRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandServiceImpl implements TeamCommandService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TeamRepository teamRepository;

    @Override
    public TeamCreateResponseDto createTeam(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException(ErrorStatus.PRODUCT_NOT_FOUND));

        Team team = Team.builder()
                .leader(user)
                .product(product)
                .status(TeamStatus.MATCHING)
                .expireAt(LocalDateTime.now().plusMinutes(30))
                .build();

        teamRepository.save(team);

        return TeamCreateResponseDto.from(team);
    }

    @Override
    public void joinTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ErrorStatus.USER_NOT_FOUND));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new BadRequestException(ErrorStatus.TEAM_NOT_FOUND));

        if (!team.getTeamMembers().isEmpty()) {
            throw new BadRequestException(ErrorStatus.TEAM_ALREADY_HAS_MEMBER);
        }
        if (team.getLeader().getId().equals(user.getId())) {
            throw new BadRequestException(ErrorStatus.CANNOT_JOIN_OWN_TEAM);
        }

        TeamMember teamMember = TeamMember.builder()
                .member(user)
                .team(team)
                .build();

        team.getTeamMembers().add(teamMember);
        team.updateStatus(TeamStatus.COMPLETED);

        // TODO: 팀 매칭이 완료되었음을 알리는 알림을 전송
    }

}
