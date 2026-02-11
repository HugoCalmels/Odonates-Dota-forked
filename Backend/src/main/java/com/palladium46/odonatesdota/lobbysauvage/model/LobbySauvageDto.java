package com.palladium46.odonatesdota.lobbysauvage.model;

import com.palladium46.odonatesdota.user.model.UserDto;

import java.util.Date;
import java.util.Set;

public class LobbySauvageDto {

    private Long id;
    private Set<UserDto> users;
    private String status;
    private Date createDateTime;
    private String lobbyName;

    public LobbySauvageDto() {}

    public LobbySauvageDto(Long id, Set<UserDto> users, String status, Date createDateTime, String lobbyName) {
        this.id = id;
        this.users = users;
        this.status = status;
        this.createDateTime = createDateTime;
        this.lobbyName = lobbyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
