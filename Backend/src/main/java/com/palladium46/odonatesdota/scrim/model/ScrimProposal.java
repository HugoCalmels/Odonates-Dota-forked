package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.scrim.model.enums.ProposalStatus;
import com.palladium46.odonatesdota.team.model.Team;
import jakarta.persistence.*;

@Entity
public class ScrimProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team proposerTeam;

    @ManyToOne
    private ScrimTeam proposerScrimTeam;

    @ManyToOne
    @JoinColumn(name = "scrim_id")
    private Scrim scrim;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public Team getProposerTeam() {
        return proposerTeam;
    }

    public void setProposerTeam(Team proposerTeam) {
        this.proposerTeam = proposerTeam;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public ScrimTeam getProposerScrimTeam() {
        return proposerScrimTeam;
    }

    public void setProposerScrimTeam(ScrimTeam proposerScrimTeam) {
        this.proposerScrimTeam = proposerScrimTeam;
    }
}