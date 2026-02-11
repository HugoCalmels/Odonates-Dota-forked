import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ScrimState } from './scrim.reducer';

const selectScrimState = createFeatureSelector<ScrimState>('scrim');

export const selectScrims = createSelector(
  selectScrimState,
  (state: ScrimState) => state.scrims
);

export const selectCurrentScrim = createSelector(
  selectScrimState,
  (state: ScrimState) => state.currentScrim
);

export const selectUserStatus = createSelector(
  selectScrimState,
  (state: ScrimState) => state.userStatus
);

export const getError = createSelector(
  selectScrimState,
  (state: ScrimState) => state.error
);

