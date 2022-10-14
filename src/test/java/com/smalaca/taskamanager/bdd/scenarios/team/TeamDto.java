package com.smalaca.taskamanager.bdd.scenarios.team;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamDto {
    private Long id;
    private String name;
    private List<Long> userIds = new ArrayList<>();
}
