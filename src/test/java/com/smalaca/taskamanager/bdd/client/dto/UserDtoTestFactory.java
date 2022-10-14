package com.smalaca.taskamanager.bdd.client.dto;

import com.smalaca.taskamanager.dto.UserDto;

import java.util.UUID;

public class UserDtoTestFactory {
    public static UserDto create(String firstName, String lastName) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setTeamRole("DEVELOPER");
        userDto.setLogin(firstName + " " + lastName);
        userDto.setPassword(UUID.randomUUID().toString());

        return userDto;
    }
}
