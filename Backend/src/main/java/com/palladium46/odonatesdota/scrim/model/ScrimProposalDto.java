package com.palladium46.odonatesdota.scrim.model;

import com.palladium46.odonatesdota.scrim.model.enums.ProposalStatus;
import com.palladium46.odonatesdota.team.model.TeamDto;

public class ScrimProposalDto {

    private Long id;
    private TeamDto proposerTeam;

    private ScrimTeamDto proposerScrimTeam;
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


    public TeamDto getProposerTeam() {
        return proposerTeam;
    }

    public void setProposerTeam(TeamDto proposerTeam) {
        this.proposerTeam = proposerTeam;
    }

    public ScrimTeamDto getProposerScrimTeam() {
        return proposerScrimTeam;
    }

    public void setProposerScrimTeam(ScrimTeamDto proposerScrimTeam) {
        this.proposerScrimTeam = proposerScrimTeam;
    }
}
