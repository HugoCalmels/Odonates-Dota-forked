import { Role } from "./role.enum";
import { Team } from "./team.model";

export interface User {
  id: string;
  steamId64bits: string;
  steamId32bits: string;
  avatar: string;
  userName: string;
  team: Team;
  roles: Role[];
  profileUrl: string;
  rankTier: number;
  userOrder: number;
  leaderboardRank: number;
  country: string;
}