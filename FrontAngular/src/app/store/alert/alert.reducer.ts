import { createReducer, on } from '@ngrx/store';
import * as AlertActions from './alert.actions';

export interface AlertState {
  alertType: string | null;
  message: string | null;
  isVisible: boolean;
}

export const initialState: AlertState = {
  alertType: null,
  message: null,
  isVisible: false
};

export const alertReducer = createReducer(
  initialState,
  on(AlertActions.setAlert, (state, { alertType, message }) => ({
    ...state,
    alertType,
    message,
    isVisible: true
  })),
  on(AlertActions.clearAlert, state => ({
    ...state,
    alertType: null,
    message: null,
    isVisible: false
  }))
);