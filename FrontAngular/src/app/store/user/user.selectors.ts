import { createFeatureSelector, createSelector } from '@ngrx/store';
import { UserState } from './user.reducer';

export const selectUserState = createFeatureSelector<UserState>('user');

export const selectUser = createSelector(selectUserState, state => state.user);

export const selectHasOneTeam = createSelector(selectUserState, state => state.hasOneTeam);