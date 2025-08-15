package com.jajaja.domain.team.repository;

import com.jajaja.domain.cart.entity.QCart;
import com.jajaja.domain.member.entity.QMember;
import com.jajaja.domain.member.entity.QMemberBusinessCategory;
import com.jajaja.domain.order.entity.QOrder;
import com.jajaja.domain.product.entity.QProduct;
import com.jajaja.domain.team.entity.QTeam;
import com.jajaja.domain.team.entity.QTeamMember;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Optional<Team> findByIdWithLeaderMembersAndProduct(Long teamId) {
        QTeam team = QTeam.team;
        QMember leader = QMember.member;
        QTeamMember teamMember = QTeamMember.teamMember;
        QProduct product = QProduct.product;
        QOrder order = QOrder.order;

        Team result = queryFactory
                .select(team)
                .distinct()
                .from(team)
                .join(team.leader, leader).fetchJoin()
                .leftJoin(team.teamMembers, teamMember).fetchJoin()
                .join(team.product, product).fetchJoin()
                .leftJoin(team.order, order).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Team> findExpiredTeamsWithAll(TeamStatus status, LocalDateTime now) {
        QTeam team = QTeam.team;
        QMember leader = QMember.member;
        QProduct product = QProduct.product;
        QCart cart = QCart.cart;
        QMemberBusinessCategory mbc = QMemberBusinessCategory.memberBusinessCategory;
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(team)
                .join(team.leader, leader).fetchJoin()
                .leftJoin(leader.cart, cart).fetchJoin()
                .leftJoin(leader.memberBusinessCategory, mbc).fetchJoin()
                .join(team.product, product).fetchJoin()
                .leftJoin(team.order, order).fetchJoin()
                .where(team.status.eq(status)
                        .and(team.expireAt.loe(now)))
                .distinct()
                .fetch();
    }

    @Override
    public Page<Team> findAllMatchingTeams(Pageable pageable) {
        QTeam team = QTeam.team;
        QProduct product = QProduct.product;
        QMember leader = QMember.member;

        List<Team> teams = queryFactory
                .selectFrom(team)
                .join(team.product, product).fetchJoin()
                .join(team.leader, leader).fetchJoin()
                .where(team.status.eq(TeamStatus.MATCHING))
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = teams.size() > pageable.getPageSize();

        if (hasNext) {
            teams.remove(teams.size() - 1);
        }

        return new PageImpl<>(teams, pageable, hasNext ? pageable.getOffset() + pageable.getPageSize() + 1 : pageable.getOffset() + teams.size());
    }
}
