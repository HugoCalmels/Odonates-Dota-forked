import { GameMode } from "./enums/gameMode.enum";
import { GameStatus } from "./enums/gameStatus.enum";
import { GameType } from "./enums/gameType.enum";
import { PreGameStatus } from "./enums/preGameStatus.enum";
import { ScrimProposal } from "./scrimProposal";
import { ScrimTeam } from "./scrimTeam.model";

export interface Scrim {
  id?: number | null;
  gameMode: GameMode;
  gameType?: GameType;
  gameStatus?: GameStatus | null;
  preGameStatus?: PreGameStatus | null;
  firstScrimTeam?: ScrimTeam;
  secondScrimTeam?: ScrimTeam;
  startDateTime: Date ;
  minMmrAccepted?: number;
  maxMmrAccepted?: number;
  lobbyName?: string | null;
  lobbyPassword?: string | null;
  firstTeamHasFinished?: boolean | null;
  secondTeamHasFinished?: boolean | null;
  proposals?: ScrimProposal[];
  selectedPlayerIds?: string[];
}