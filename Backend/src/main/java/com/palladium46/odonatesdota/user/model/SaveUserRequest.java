package com.palladium46.odonatesdota.user.model;

import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;

public class SaveUserRequest {
    private String name;
    private String avatar;
    private Integer seasonRank;
    private String steamId64bits;

    private String profileUrl;

    private Integer leaderboardRank;

    private String country;

    public String getSteamId64bits() {
        return steamId64bits;
    }

    public void setSteamId64bits(String steamId64bits) {
        this.steamId64bits = steamId64bits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSeasonRank() {
        return seasonRank;
    }

    public void setSeasonRank(Integer seasonRank) {
        this.seasonRank = seasonRank;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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