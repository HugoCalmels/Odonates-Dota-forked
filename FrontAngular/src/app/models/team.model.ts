import { TeamStatus } from "./teamStatus.enum";
import { User } from "./user.model";

export interface Team {
  id: string | null;
  name: string | null;
  password: string | null;
  logo: string | null;
  logoName: string | null;
  captain: User | null;
  users: User[] | null;
  createdAt: Date | string | null; 
  acronym: string | null;
  status?: TeamStatus;
}