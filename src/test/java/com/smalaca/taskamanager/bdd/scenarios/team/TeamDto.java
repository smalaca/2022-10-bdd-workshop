package com.smalaca.taskamanager.bdd.scenarios.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamDto {
    private Long id;
    private String name;
}
