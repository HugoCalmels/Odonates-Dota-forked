import { createAction, props } from '@ngrx/store';
import { User } from 'src/app/models/user.model';

export const UserActions = {
  setUser: createAction('[User] Set User', props<{ user: User }>()),
  clearUser: createAction('[User] Clear User'),
  setHasOneTeam: createAction('[User] Set Has One Team', props<{ hasOneTeam: boolean }>())
};