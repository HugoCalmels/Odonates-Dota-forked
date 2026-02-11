import { AuthState } from './store/auth/auth.reducer';
import { UserState } from './store/user/user.reducer';
import { TeamState } from './store/team/team.reducer';
import { ScrimState } from './store/scrim/scrim.reducer';
import { AlertState } from './store/alert/alert.reducer';

export interface AppState {
  auth: AuthState;
  user: UserState;
  team: TeamState;
  scrim: ScrimState;
  alert: AlertState;
}