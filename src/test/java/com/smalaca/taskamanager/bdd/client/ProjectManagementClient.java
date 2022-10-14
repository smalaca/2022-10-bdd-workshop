package com.smalaca.taskamanager.bdd.client;

import com.smalaca.taskamanager.bdd.scenarios.team.TeamDto;
import com.smalaca.taskamanager.dto.TeamMembersDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ProjectManagementClient {
    private final RestTemplate restTemplate;
    private final String url;

    ProjectManagementClient(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public void deleteAllTeams() {
        TeamDto[] teamDtos = restTemplate.getForObject(teamUrl(), TeamDto[].class);
        Arrays.asList(teamDtos).forEach(dto -> {
            TeamMembersDto teamMembersDto = new TeamMembersDto();
            teamMembersDto.setUserIds(dto.getUserIds());
            HttpEntity<TeamMembersDto> entity = new HttpEntity<>(teamMembersDto);
            restTemplate.exchange(teamUrl() + dto.getId() + "/members", HttpMethod.DELETE, entity, Void.class);
            restTemplate.delete(teamUrl() + dto.getId());
        });
    }

    private String teamUrl() {
        return url + "team/";
    }
}
