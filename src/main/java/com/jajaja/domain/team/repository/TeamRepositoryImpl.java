package com.jajaja.domain.team.repository;

import com.jajaja.domain.member.entity.QMember;
import com.jajaja.domain.team.entity.QTeam;
import com.jajaja.domain.team.entity.QTeamMember;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findMatchingTeamsByProductId(Long productId) {
        QTeam team = QTeam.team;
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(team)
                .join(team.leader, member).fetchJoin()
                .where(
                        team.product.id.eq(productId),
                        team.status.eq(TeamStatus.MATCHING),
                        team.teamMembers.isEmpty()
                )
                .orderBy(team.expireAt.asc())
                .fetch();
    }

    @Override
    public Optional<Team> findByIdWithLeaderAndMembers(Long teamId) {
        QTeam team = QTeam.team;
        QMember leader = QMember.member;
        QTeamMember teamMember = QTeamMember.teamMember;

        List<Team> result = queryFactory
                .selectFrom(team)
                .join(team.leader, leader).fetchJoin()
                .leftJoin(team.teamMembers, teamMember).fetchJoin()
                .where(team.id.eq(teamId))
                .fetch();

        return result.stream().findFirst();
    }

    @Override
    public List<Team> findExpiredTeamsWithLeader(TeamStatus status, LocalDateTime now) {
        QTeam team = QTeam.team;
        QMember leader = QMember.member;

        return queryFactory
                .selectFrom(team)
                .join(team.leader, leader).fetchJoin()
                .where(team.status.eq(status)
                        .and(team.expireAt.loe(now)))
                .fetch();
    }
}
