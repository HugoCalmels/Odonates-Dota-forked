package com.palladium46.odonatesdota.scrim.repository;

import com.palladium46.odonatesdota.scrim.model.Scrim;
import com.palladium46.odonatesdota.scrim.model.ScrimProposal;
import com.palladium46.odonatesdota.scrim.model.enums.ProposalStatus;
import com.palladium46.odonatesdota.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrimProposalRepository extends JpaRepository<ScrimProposal, Long> {
    ScrimProposal findByProposerTeamAndScrim(Team proposerTeam, Scrim scrim);

    ScrimProposal findFirstByProposerTeamAndScrimAndStatus(Team proposerTeam, Scrim scrim, ProposalStatus status);

    List<ScrimProposal> findByProposerTeam(Team team);
}
