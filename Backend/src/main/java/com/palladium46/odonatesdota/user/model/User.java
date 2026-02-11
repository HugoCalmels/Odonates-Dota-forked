package com.palladium46.odonatesdota.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.palladium46.odonatesdota.lobbysauvage.model.LobbySauvage;
import com.palladium46.odonatesdota.security._refreshToken.model.RefreshToken;
import com.palladium46.odonatesdota.team.model.Team;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "app_user")
public class User {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_name")
    private String userName;

    @Column
    private String steamId64bits;

    @Column
    private String steamId32bits;

    @Column(nullable = true)
    private Integer rankTier;

    @Column
    private String avatar;

    @Column
    private LocalDateTime lastEdit;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", length = 20)
    private Set<Role> roles;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;


    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private LobbySauvage lobbySauvage;

    private int userOrder;

    private String profileUrl;

    @Column(name = "is_banned", nullable = false)
    private boolean isBanned;

    @Column(nullable = true)
    private Integer leaderboardRank;

    private String country;

    public User() {
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public UUID getId() {
        return id;
    }

    public LobbySauvage getLobbySauvage() {
        return lobbySauvage;
    }

    public void setLobbySauvage(LobbySauvage lobbySauvage) {
        this.lobbySauvage = lobbySauvage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return this.getId().equals(user.getId()) &&
                Objects.equals(this.getSteamId64bits(), user.getSteamId64bits());
    }

    public int getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(int userOrder) {
        this.userOrder = userOrder;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }


    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
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
