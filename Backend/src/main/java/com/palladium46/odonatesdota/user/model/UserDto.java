package com.palladium46.odonatesdota.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.palladium46.odonatesdota.team.model.TeamDto;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;

    public String steamId64bits;

    public String steamId32bits;

    private Integer rankTier;

    private String avatar;

    private LocalDateTime lastEdit;

    private String userName;

    private TeamDto team;

    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Role> roles;

    private String profileUrl;

    private int userOrder;

    private boolean banned;

    private Integer leaderboardRank;

    private String country;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getSteamId64bits() {
        return steamId64bits;
    }

    public void setSteamId64bits(String steamId64bits) {
        this.steamId64bits = steamId64bits;
    }

    public String getSteamId32bits() {
        return steamId32bits;
    }

    public void setSteamId32bits(String steamId32bits) {
        this.steamId32bits = steamId32bits;
    }

    public Integer getRankTier() {
        return rankTier;
    }

    public void setRankTier(Integer rankTier) {
        this.rankTier = rankTier;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(LocalDateTime lastLogin) {
        this.lastEdit = lastLogin;
    }

    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(int userOrder) {
        this.userOrder = userOrder;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isBanned() {
        return banned;
    }

    public Integer getLeaderboardRank() {
        return leaderboardRank;
    }

    public void setLeaderboardRank(Integer leaderboardRank) {
        this.leaderboardRank = leaderboardRank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
