package com.smalaca.taskamanager.bdd.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamDto {
    private Long id;
    private String name;
    private Long leaderId;
    private List<Long> userIds = new ArrayList<>();
}
