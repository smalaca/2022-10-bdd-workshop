package com.smalaca.taskamanager.bdd.scenarios.team;

import com.smalaca.taskamanager.bdd.scenarios.JBehaveConfiguration;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamScenarios extends JBehaveConfiguration {
    private static final String TEAM_URL = "http://localhost:8080/team";

    private final RestTemplate restTemplate = restTemplate();

    private List<TeamDto> teamDtos;

    private ResponseEntity<Void> response;
    @BeforeScenario
    public void removeAllTeams() {
        TeamDto[] dtos = restTemplate.getForObject(TEAM_URL, TeamDto[].class);
        Arrays.asList(dtos).forEach(teamDto -> {
            restTemplate.delete(TEAM_URL + "/" + teamDto.getId());
        });

        teamDtos = new ArrayList<>();
    }

    private RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {

            }
        });

        return restTemplate;
    }

    @When("Project Manager checks what teams exist")
    public void findAllTeams() {
        TeamDto[] dtos = restTemplate.getForObject(TEAM_URL, TeamDto[].class);
        teamDtos = Arrays.asList(dtos);
    }

    @Then("$expectedTeamsNumber teams found")
    public void shouldFindTeams(int expectedTeamsNumber) {
        assertThat(teamDtos).hasSize(expectedTeamsNumber);
    }

    @When("Project Manager creates team $name")
    public void createTeam(String name) {
        TeamDto teamDto = TeamDto.builder().name(name).build();
        HttpEntity<TeamDto> entity = new HttpEntity<>(teamDto);

        response = restTemplate.exchange(TEAM_URL, HttpMethod.POST, entity, Void.class);
    }

    @Then("team was created")
    public void teamCreated() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("no new team created")
    public void teamNotCreated() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
