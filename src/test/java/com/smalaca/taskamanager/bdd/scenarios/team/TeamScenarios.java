package com.smalaca.taskamanager.bdd.scenarios.team;

import com.google.common.primitives.Longs;
import com.smalaca.taskamanager.bdd.client.ProjectManagementClient;
import com.smalaca.taskamanager.bdd.client.ProjectManagementClientFactory;
import com.smalaca.taskamanager.bdd.scenarios.JBehaveConfiguration;
import com.smalaca.taskamanager.dto.TeamMembersDto;
import com.smalaca.taskamanager.dto.UserDto;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamScenarios extends JBehaveConfiguration {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String TEAM_URL = BASE_URL + "team/";
    private static final String USER_URL = BASE_URL + "user/";

    private final RestTemplate restTemplate = restTemplate();
    private final ProjectManagementClient client = ProjectManagementClientFactory.create(BASE_URL);
    private List<TeamDto> teamDtos;
    private Map<String, Long> users;
    private Map<String, Long> teams;
    private ResponseEntity<Void> response;

    @BeforeScenario
    public void removeAllTeams() {
        client.deleteAllTeams();
        UserDto[] userDtos = restTemplate.getForObject(USER_URL, UserDto[].class);
        Arrays.asList(userDtos).forEach(dto -> {
            restTemplate.delete(USER_URL + dto.getId());
        });

        this.teamDtos = new ArrayList<>();
        users = new HashMap<>();
        teams = new HashMap<>();
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

    @Then("team with name $expectedName exist")
    public void shouldFindTeamWithName(String expectedName) {
        assertThat(teamDtos).anySatisfy(dto -> {
            assertThat(dto.getName()).isEqualTo(expectedName);
        });
    }

    @When("Project Manager creates team $name")
    public void createTeam(String name) {
        TeamDto teamDto = new TeamDto();
        teamDto.setName(name);
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

    @Given("existing team $name")
    public void givenExistingTeam(String name) {
        TeamDto teamDto = new TeamDto();
        teamDto.setName(name);
        HttpEntity<TeamDto> entity = new HttpEntity<>(teamDto);

        ResponseEntity<Void> response = restTemplate.exchange(TEAM_URL, HttpMethod.POST, entity, Void.class);

        teams.put(name, Long.valueOf(response.getHeaders().getLocation().toString().replace(TEAM_URL, "")));
    }

    @Given("User $firstName $lastName")
    public void givenUser(String firstName, String lastName) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setTeamRole("DEVELOPER");
        userDto.setLogin(firstName + " " + lastName);
        userDto.setPassword(UUID.randomUUID().toString());
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);

        ResponseEntity<Void> response = restTemplate.exchange(USER_URL, HttpMethod.POST, entity, Void.class);

        Long userId = Long.valueOf(response.getHeaders().getLocation().toString().replace(USER_URL, ""));
        users.put(firstName + " " + lastName, userId);
    }

    @When("Project Manager adds $teamMemberFullName to $teamName")
    public void addMemberToTeam(String teamMemberFullName, String teamName) {
        TeamMembersDto teamMembersDto = new TeamMembersDto();
        teamMembersDto.setUserIds(Longs.asList(users.get(teamMemberFullName)));

        restTemplate.put(TEAM_URL + teams.get(teamName) + "/members", teamMembersDto);
    }

    @Then("$teamName have $teamMembersAmount team member")
    public void teamShouldContainExpectedNumberOfMembers(String teamName, int teamMembersAmount) {
        TeamDto teamDto = restTemplate.getForObject(TEAM_URL + teams.get(teamName), TeamDto.class);

        assertThat(teamDto.getName()).isEqualTo(teamName);
        assertThat(teamDto.getUserIds()).hasSize(teamMembersAmount);
    }

    @Then("$teamName contains $teamMemberFullName")
    public void teamShouldContainExpectedMember(String teamName, String teamMemberFullName) {
        TeamDto teamDto = restTemplate.getForObject(TEAM_URL + teams.get(teamName), TeamDto.class);

        assertThat(teamDto.getName()).isEqualTo(teamName);
        assertThat(teamDto.getUserIds()).contains(users.get(teamMemberFullName));
    }
}
