package com.smalaca.taskamanager.bdd.assertions;

import com.smalaca.taskamanager.bdd.client.dto.TeamDto;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamDtoAssertion {
    private final TeamDto actual;

    TeamDtoAssertion(TeamDto actual) {
        this.actual = actual;
    }

    public TeamDtoAssertion hasName(String expected) {
        assertThat(actual.getName()).isEqualTo(expected);
        return this;
    }

    public TeamDtoAssertion hasTeamMember(Long expected) {
        assertThat(actual.getUserIds()).contains(expected);
        return this;
    }

    public TeamDtoAssertion hasTeamMembersAmount(int expected) {
        assertThat(actual.getUserIds()).hasSize(expected);
        return this;
    }
}
