package com.palladium46.odonatesdota.team.model;

import com.palladium46.odonatesdota.user.model.UserDto;

import java.util.Set;

public class TeamDto {

    private String id;

    public String name;

    private String logo;

    private String logoName;

    private Set<UserDto> users;

    private UserDto captain;

    private String createdAt;

    private String acronym;

    private TeamStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public UserDto getCaptain() {
        return captain;
    }

    public void setCaptain(UserDto captain) {
        this.captain = captain;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }
}
