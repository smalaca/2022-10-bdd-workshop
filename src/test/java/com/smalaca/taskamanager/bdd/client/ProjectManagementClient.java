package com.smalaca.taskamanager.bdd.client;

import com.smalaca.taskamanager.bdd.client.dto.TeamDto;
import com.smalaca.taskamanager.dto.TeamMembersDto;
import com.smalaca.taskamanager.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public void deleteAllUsers() {
        UserDto[] userDtos = restTemplate.getForObject(userUrl(), UserDto[].class);
        Arrays.asList(userDtos).forEach(dto -> {
            restTemplate.delete(userUrl() + dto.getId());
        });
    }

    private String userUrl() {
        return url + "user/";
    }

    public Long createTeam(TeamDto teamDto) {
        HttpEntity<TeamDto> entity = new HttpEntity<>(teamDto);
        ResponseEntity<Void> response = restTemplate.exchange(teamUrl(), HttpMethod.POST, entity, Void.class);
        String id = response.getHeaders().getLocation().toString().replace(teamUrl(), "");
        return Long.valueOf(id);
    }

    public Long createUser(UserDto userDto) {
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);
        ResponseEntity<Void> response = restTemplate.exchange(userUrl(), HttpMethod.POST, entity, Void.class);
        return Long.valueOf(response.getHeaders().getLocation().toString().replace(userUrl(), ""));
    }
}
