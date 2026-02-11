import { createReducer, on } from '@ngrx/store';
import { UserActions } from './user.actions';
import { User } from 'src/app/models/user.model';

export interface UserState {
  user: User | null;
  hasOneTeam: boolean;
}

export const initialState: UserState = {
  user: null,
  hasOneTeam: false
};

export const userReducer = createReducer(
  initialState,
  on(UserActions.setUser, (state, { user }) => ({ ...state, user })),
  on(UserActions.clearUser, state => ({ ...state, user: null })),
  on(UserActions.setHasOneTeam, (state, { hasOneTeam }) => ({ ...state, hasOneTeam }))
);