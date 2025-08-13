package com.jajaja.domain.team.service;

import com.jajaja.domain.notification.dto.request.NotificationCreateRequestDto;
import com.jajaja.domain.notification.entity.enums.NotificationType;
import com.jajaja.domain.notification.service.NotificationService;
import com.jajaja.domain.product.entity.Product;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.TeamMember;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommonService {

    private final NotificationService notificationService;

    public void joinTeam(Member member, Member leader, Team team) {
        if (!team.getTeamMembers().isEmpty()) {
            throw new BadRequestException(ErrorStatus.TEAM_ALREADY_HAS_MEMBER);
        }
        if (team.getLeader().getId().equals(member.getId())) {
            throw new BadRequestException(ErrorStatus.CANNOT_JOIN_OWN_TEAM);
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();

        team.getTeamMembers().add(teamMember);
        team.updateStatus(TeamStatus.COMPLETED);

        Product product = team.getProduct();

        notificationService.createNotification(
                NotificationCreateRequestDto.of(
                        leader.getId(),
                        NotificationType.MATCHING,
                        "팀 매칭 완료",
                        Map.of(
                                "productId", product.getId(),
                                "productName", product.getName(),
                                "productImage", product.getThumbnailUrl(),
                                "isTeamMatched", true
                        )
                )
        );
    }
}
