import { createReducer, on } from '@ngrx/store';
import * as ScrimActions from './scrim.actions';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { ScrimDetailsResponse } from 'src/app/models/scrim/scrimDetailsResponse';

export interface ScrimState {
  scrims: Scrim[]; 
  currentScrim: Scrim | null;
  error: any;
  userStatus: ScrimDetailsResponse | null;
}

export const initialState: ScrimState = {
  scrims: [],
  currentScrim: null,
  error: null,
  userStatus: null,
};

export const scrimReducer = createReducer(
  initialState,
  on(ScrimActions.loadScrimListSuccess, (state, { scrims }) => ({
    ...state,
    scrims
  })),
  on(ScrimActions.loadScrimListFailure, state => ({
    ...state,
    scrims: [] 
  })),
  on(ScrimActions.createScrimSuccess, (state, { scrim }) => {
    return {
      ...state,
      scrims: [...state.scrims, scrim]
    };
  }),
  on(ScrimActions.createScrimFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.loadScrimDetailsSuccess, (state, { scrim }) => ({
    ...state,
    scrims: state.scrims.map(s => s.id === scrim.id ? scrim : s),
    currentScrim: scrim, 
  })),
  on(ScrimActions.loadScrimDetailsFailure, (state, { error }) => ({
    ...state,
    error : error
  })),
  on(ScrimActions.getScrimUserStatusSuccess, (state, { userStatus }) => ({
    ...state,
    userStatus: userStatus
  })),
  on(ScrimActions.getScrimUserStatusFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.joinScrimSuccess, (state, { scrim }) => ({
    ...state,
    scrims: state.scrims.map(s => s.id === scrim.id ? scrim : s),
    currentScrim: scrim,
    userStatus: state.userStatus ? {
      ...state.userStatus,
      canJoinScrim: false,
      canCancelScrim: true
    } : null
  })),
  on(ScrimActions.joinScrimFailure, (state, { error }) => ({
    ...state,
  })),
  on(ScrimActions.deleteScrimSuccess, (state, { scrimId }) => ({
    ...state,
    scrims: state.scrims.filter(scrim => scrim.id !== scrimId),
    currentScrim: null, 
    error: null
  })),
  on(ScrimActions.deleteScrimFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.cancelScrimSuccess, (state, { scrim }) => ({
    ...state,
    scrims: state.scrims.map(s => s.id === scrim.id ? scrim : s),
    currentScrim: scrim, 
    userStatus: state.userStatus ? {
      ...state.userStatus,
      canJoinScrim: true,
      canCancelScrim: false
    } : null
  })),
  on(ScrimActions.cancelScrimFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.loadScrimHistorySuccess, (state, { scrims }) => ({
    ...state,
    scrims
  })),
  on(ScrimActions.loadScrimHistoryFailure, state => ({
    ...state,
    scrims: [] 
  })),
  on(ScrimActions.loadScrimProposalsSuccess, (state, { scrimProposals }) => ({
    ...state,
    scrimProposals: [...scrimProposals]
  })),
  on(ScrimActions.loadScrimProposalsFailure, state => ({
    ...state,
    scrimProposals: [] 
  })),
  on(ScrimActions.sendScrimProposalSuccess, (state, { scrim }) => ({
    ...state,
    currentScrim: scrim, 
    userStatus: state.userStatus ? {
      ...state.userStatus,
      hasAlreadySentProposal: true,
    } : null
  })),
  on(ScrimActions.sendScrimProposalFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.cancelScrimProposalSuccess, (state, { scrim }) => ({
    ...state,
    currentScrim: scrim, 
    userStatus: state.userStatus ? {
      ...state.userStatus,
      hasAlreadySentProposal: false,
    } : null
  })),
  on(ScrimActions.cancelScrimProposalFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.acceptScrimProposalSuccess, (state, { scrim }) => ({
    ...state,
    currentScrim: scrim, 
    userStatus: state.userStatus ? {
      ...state.userStatus,
      hasAlreadySentProposal: false,
    } : null
  })),
  on(ScrimActions.acceptScrimProposalFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(ScrimActions.rejectScrimProposalSuccess, (state, { scrim }) => ({
    ...state,
    currentScrim: scrim, 
    userStatus: state.userStatus ? {
      ...state.userStatus,
      hasAlreadySentProposal: false,
    } : null
  })),
  on(ScrimActions.rejectScrimProposalFailure, (state, { error }) => ({
    ...state,
    error
  })),
);