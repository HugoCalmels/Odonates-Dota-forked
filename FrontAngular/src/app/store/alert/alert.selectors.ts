import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AlertState } from './alert.reducer';

export const selectAlertState = createFeatureSelector<AlertState>('alert');

export const selectAlert = createSelector(
  selectAlertState,
  (state: AlertState) => state
);