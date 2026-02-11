package com.palladium46.odonatesdota.steamAuth.model;

public class PlayerInfo {

    private String name;
    private Integer seasonRank;
    private String avatar;
    private String profileUrl;
    private Integer leaderboardRanking;
    private String country;

    public PlayerInfo(String name, String avatar, Integer seasonRank, String profileUrl, Integer leaderboardRanking, String country) {
        this.name = name;
        this.avatar = avatar;
        this.seasonRank = seasonRank;
        this.profileUrl = profileUrl;
        this.leaderboardRanking = leaderboardRanking;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeasonRank() {
        return seasonRank;
    }

    public void setSeasonRank(Integer seasonRank) {
        this.seasonRank = seasonRank;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Integer getLeaderboardRanking() {
        return leaderboardRanking;
    }

    public void setLeaderboardRanking(Integer leaderboardRanking) {
        this.leaderboardRanking = leaderboardRanking;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
