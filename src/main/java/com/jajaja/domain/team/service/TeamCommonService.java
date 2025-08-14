package com.jajaja.domain.team.service;

import com.jajaja.domain.member.entity.Member;
import com.jajaja.domain.team.entity.Team;

public interface TeamCommonService {
    void joinTeam(Member member, Member leader, Team team);
}
