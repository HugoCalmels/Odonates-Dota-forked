import {User} from "../../models/user.model";

export enum LobbyStatus {
  WAITING_FOR_PLAYER = "WAITING_FOR_PLAYER",
  READY = "READY",
  CLOSED = "CLOSED",
  LAUNCHED = "LAUNCHED"
}

export interface LobbySauvage {
  id: number;
  users: User[];
  status: LobbyStatus;
  createDateTime: Date;
  lobbyName: string;
}
