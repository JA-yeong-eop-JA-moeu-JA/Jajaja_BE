package com.jajaja.domain.team.service;

import com.jajaja.domain.team.dto.response.TeamProductItemResponseDto;
import com.jajaja.domain.team.dto.response.TeamProductListResponseDto;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.repository.TeamRepository;
import com.jajaja.global.S3.service.S3Service;
import com.jajaja.global.apiPayload.PageResponse;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final S3Service s3Service;

    @Override
    public TeamProductListResponseDto getMatchingTeamProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Team> teamPage = teamRepository.findAllMatchingTeams(pageable);

        if (teamPage.isEmpty()) {
            throw new BadRequestException(ErrorStatus.TEAM_NOT_FOUND);
        }

        List<TeamProductItemResponseDto> teamDtos = teamPage.getContent().stream()
                .map(team -> {
                    String profileUrl = s3Service.generateStaticUrl(team.getLeader().getProfileKeyName());
                    return TeamProductItemResponseDto.of(team, profileUrl);
                })
                .toList();

        Page<TeamProductItemResponseDto> mappedPage = new PageImpl<>(teamDtos, pageable, teamPage.getTotalElements());

        return new TeamProductListResponseDto(PageResponse.from(mappedPage), teamDtos);
    }
}