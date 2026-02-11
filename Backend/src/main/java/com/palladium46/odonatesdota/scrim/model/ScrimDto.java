package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.scrim.model.enums.GameMode;
import com.palladium46.odonatesdota.scrim.model.enums.GameStatus;
import com.palladium46.odonatesdota.scrim.model.enums.GameType;
import com.palladium46.odonatesdota.scrim.model.enums.PreGameStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ScrimDto {
    private Long id;

    private GameMode gameMode;

    private GameType gameType;

    private GameStatus gameStatus;

    private PreGameStatus preGameStatus;

    private LocalDateTime startDateTime;

    private Integer minMmrAccepted;

    private Integer maxMmrAccepted;

    private ScrimTeamDto firstScrimTeam;

    private ScrimTeamDto secondScrimTeam;

    private String lobbyName;

    private String lobbyPassword;

    private boolean firstTeamHasFinished;

    private boolean secondTeamHasFinished;

    private List<ScrimProposalDto> proposals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public PreGameStatus getPreGameStatus() {
        return preGameStatus;
    }

    public void setPreGameStatus(PreGameStatus preGameStatus) {
        this.preGameStatus = preGameStatus;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Integer getMinMmrAccepted() {
        return minMmrAccepted;
    }

    public void setMinMmrAccepted(Integer minMmrAccepted) {
        this.minMmrAccepted = minMmrAccepted;
    }

    public Integer getMaxMmrAccepted() {
        return maxMmrAccepted;
    }

    public void setMaxMmrAccepted(Integer maxMmrAccepted) {
        this.maxMmrAccepted = maxMmrAccepted;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public boolean isFirstTeamHasFinished() {
        return firstTeamHasFinished;
    }

    public void setFirstTeamHasFinished(boolean firstTeamHasFinished) {
        this.firstTeamHasFinished = firstTeamHasFinished;
    }

    public boolean isSecondTeamHasFinished() {
        return secondTeamHasFinished;
    }

    public void setSecondTeamHasFinished(boolean secondTeamHasFinished) {
        this.secondTeamHasFinished = secondTeamHasFinished;
    }

    public String getLobbyPassword() {
        return lobbyPassword;
    }

    public void setLobbyPassword(String lobbyPassword) {
        this.lobbyPassword = lobbyPassword;
    }

    public List<ScrimProposalDto> getProposals() {
        return proposals;
    }

    public void setProposals(List<ScrimProposalDto> proposals) {
        this.proposals = proposals;
    }

    public ScrimTeamDto getFirstScrimTeam() {
        return firstScrimTeam;
    }

    public void setFirstScrimTeam(ScrimTeamDto firstScrimTeam) {
        this.firstScrimTeam = firstScrimTeam;
    }

    public ScrimTeamDto getSecondScrimTeam() {
        return secondScrimTeam;
    }

    public void setSecondScrimTeam(ScrimTeamDto secondScrimTeam) {
        this.secondScrimTeam = secondScrimTeam;
    }
}
