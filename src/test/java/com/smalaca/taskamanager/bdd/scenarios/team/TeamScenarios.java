package com.smalaca.taskamanager.bdd.scenarios.team;

import com.google.common.primitives.Longs;
import com.smalaca.taskamanager.bdd.client.ProjectManagementClient;
import com.smalaca.taskamanager.bdd.client.ProjectManagementClientFactory;
import com.smalaca.taskamanager.bdd.client.dto.TeamDto;
import com.smalaca.taskamanager.bdd.client.dto.UserDtoTestFactory;
import com.smalaca.taskamanager.bdd.scenarios.JBehaveConfiguration;
import com.smalaca.taskamanager.dto.TeamMembersDto;
import com.smalaca.taskamanager.dto.UserDto;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

import static com.smalaca.taskamanager.bdd.assertions.AssertionsFacade.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class TeamScenarios extends JBehaveConfiguration {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String TEAM_URL = BASE_URL + "team/";

    private final RestTemplate restTemplate = restTemplate();
    private final ProjectManagementClient client = ProjectManagementClientFactory.create(BASE_URL);
    private List<TeamDto> teamDtos;
    private Map<String, Long> users;
    private Map<String, Long> teams;
    private ResponseEntity<Void> response;

    @BeforeScenario
    public void removeAllTeams() {
        client.deleteAllTeams();
        client.deleteAllUsers();

        teamDtos = new ArrayList<>();
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

    @Then("team with name $expectedName and leader $teamMemberFullName exist")
    public void shouldFindTeamWithName(String expectedName, String teamMemberFullName) {
//        assertThat(teamDtos)
//                .hasTeam(expectedName)
//                .hasLeader(users.get(teamMemberFullName));

        assertThat(teamDtos).anySatisfy(dto -> {
            assertThat(dto.getName()).isEqualTo(expectedName);
            assertThat(dto.getLeaderId()).isEqualTo(users.get(teamMemberFullName));
        });
    }

    @When("Project Manager creates team $name with leader $teamMemberFullName")
    public void createTeam(String name, String teamMemberFullName) {
        TeamDto teamDto = new TeamDto();
        teamDto.setName(name);
        teamDto.setLeaderId(users.get(teamMemberFullName));

        HttpEntity<TeamDto> entity = new HttpEntity<>(teamDto);
        response = restTemplate.exchange(TEAM_URL, HttpMethod.POST, entity, Void.class);

        updateTeamId(name);
    }

    private void updateTeamId(String name) {
        HttpHeaders headers = response.getHeaders();

        if (headers.getLocation() != null) {
            String id = headers.getLocation().toString().replace(TEAM_URL, "");
            teams.put(name, Long.valueOf(id));
        }
    }

    @Then("team was created")
    public void teamCreated() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("no new team created")
    public void teamNotCreated() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Given("User $firstName $lastName")
    public void givenUser(String firstName, String lastName) {
        UserDto userDto = UserDtoTestFactory.create(firstName, lastName);

        Long id = client.createUser(userDto);

        users.put(firstName + " " + lastName, id);
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

        assertThat(teamDto)
                .hasName(teamName)
                .hasTeamMembersAmount(teamMembersAmount);
    }

    @Then("$teamName contains $teamMemberFullName")
    public void teamShouldContainExpectedMember(String teamName, String teamMemberFullName) {
        TeamDto teamDto = restTemplate.getForObject(TEAM_URL + teams.get(teamName), TeamDto.class);

        assertThat(teamDto)
                .hasName(teamName)
                .hasTeamMember(users.get(teamMemberFullName));
    }
}
