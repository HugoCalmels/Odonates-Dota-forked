package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.team.model.TeamDto;
import com.palladium46.odonatesdota.user.model.UserDto;

import java.util.Set;

public class ScrimTeamDto {

    private Long id;

    private TeamDto team;

    private Set<UserDto> users;

    private UserDto squadCaptain;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public TeamDto getTeam() {
        return team;
    }

    public Long getId() {
        return id;
    }

    public UserDto getSquadCaptain() {
        return squadCaptain;
    }

    public void setSquadCaptain(UserDto squadCaptain) {
        this.squadCaptain = squadCaptain;
    }
}
