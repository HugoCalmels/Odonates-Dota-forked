package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.scrim.model.enums.GameMode;
import com.palladium46.odonatesdota.scrim.model.enums.GameStatus;
import com.palladium46.odonatesdota.scrim.model.enums.GameType;
import com.palladium46.odonatesdota.scrim.model.enums.PreGameStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Scrim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "game-mode", length = 20)
    private GameMode gameMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "game-type", length = 20)
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "game-status", length = 20)
    private GameStatus gameStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "pre-game-status", length = 20)
    private PreGameStatus preGameStatus;

    @Column
    private LocalDateTime startDateTime;

    @Column(nullable = true)
    private Integer averageRankTier;

    @Column
    private Integer minMmrAccepted;

    @Column
    private Integer maxMmrAccepted;

    @Column
    private String lobbyName;

    @Column
    private String lobbyPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "first_scrim_team_id", referencedColumnName = "id")
    private ScrimTeam firstScrimTeam;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "second_scrim_team_id", referencedColumnName = "id")
    private ScrimTeam secondScrimTeam;

    @Column
    private boolean firstTeamHasFinished;

    @Column
    private boolean secondTeamHasFinished;

    @OneToMany(mappedBy = "scrim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScrimProposal> proposals = new ArrayList<>();

    public void addScrimProposal(ScrimProposal proposal) {
        proposals.add(proposal);
    }
    public void removeScrimProposal(ScrimProposal proposal) {
        proposals.remove(proposal);
    }

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


    public Integer getAverageRankTier() {
        return averageRankTier;
    }

    public void setAverageRankTier(Integer averageRankTier) {
        this.averageRankTier = averageRankTier;
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

    public List<ScrimProposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<ScrimProposal> proposals) {
        this.proposals = proposals;
    }

    public ScrimTeam getFirstScrimTeam() {
        return firstScrimTeam;
    }

    public void setFirstScrimTeam(ScrimTeam firstScrimTeam) {
        this.firstScrimTeam = firstScrimTeam;
    }

    public ScrimTeam getSecondScrimTeam() {
        return secondScrimTeam;
    }

    public void setSecondScrimTeam(ScrimTeam secondScrimTeam) {
        this.secondScrimTeam = secondScrimTeam;
    }
}
