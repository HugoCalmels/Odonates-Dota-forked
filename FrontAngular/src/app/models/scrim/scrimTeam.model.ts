import { Team } from "../team.model";
import { User } from "../user.model";

export interface ScrimTeam {
  id?: number | null;
  team: Team;
  users: User[];
  squadCaptain: User;
}