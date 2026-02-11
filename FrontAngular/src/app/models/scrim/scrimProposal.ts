import { Team } from "../team.model";
import { ProposalStatus } from "./enums/proposalStatus.enum";
import { Scrim } from "./scrim.model";
import { ScrimTeam } from "./scrimTeam.model";

export interface ScrimProposal { 
  id: number;
  scrim: Scrim;
  proposerTeam: Team;
  proposerScrimTeam: ScrimTeam;
  status: ProposalStatus;
}