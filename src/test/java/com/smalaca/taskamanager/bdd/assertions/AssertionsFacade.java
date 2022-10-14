package com.smalaca.taskamanager.bdd.assertions;

import com.smalaca.taskamanager.bdd.client.dto.TeamDto;

public class AssertionsFacade {
    public static TeamDtoAssertion assertThat(TeamDto teamDto) {
        return new TeamDtoAssertion(teamDto);
    }
}
