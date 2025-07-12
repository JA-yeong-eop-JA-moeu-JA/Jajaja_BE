package com.jajaja.domain.team.repository;

import com.jajaja.domain.team.entity.QTeam;
import com.jajaja.domain.team.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findMatchingTeamsByProductId(Long productId) {
        QTeam team = QTeam.team;

        return queryFactory
                .selectFrom(team)
                .where(
                        team.product.id.eq(productId),
                        team.status.eq(com.jajaja.domain.team.entity.enums.TeamStatus.MATCHING),
                        team.leader.isNotNull(),
                        team.teamMembers.isEmpty()
                )
                .fetch();
    }
}
