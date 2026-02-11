import { createAction, props } from '@ngrx/store';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { ScrimDetailsResponse } from 'src/app/models/scrim/scrimDetailsResponse';
import { ScrimProposal } from 'src/app/models/scrim/scrimProposal';

export const loadScrimList = createAction('[Scrim] Load Scrim List', props<{ timezone: string }>());
export const loadScrimListSuccess = createAction('[Scrim] Load Scrim List Success', props<{ scrims: Scrim[] }>());
export const loadScrimListFailure = createAction('[Scrim] Load Scrim List Failure', props<{ error: any }>());

export const createScrim = createAction('[Scrim] Create Scrim', props<{ newScrim: Scrim, timezone: string, selectedPlayerIds: string[] }>());
export const createScrimSuccess = createAction('[Scrim] Create Scrim Success', props<{ scrim: Scrim }>());
export const createScrimFailure = createAction('[Scrim] Create Scrim Failure', props<{ error: any  }>());


export const loadScrimDetails = createAction('[Scrim] Load Scrim Details', props<{ scrimId: number, userTimeZone: string }>());
export const loadScrimDetailsSuccess = createAction('[Scrim] Load Scrim Details Success', props<{ scrim: Scrim }>());
export const loadScrimDetailsFailure = createAction('[Scrim] Load Scrim Details Failure', props<{ error: any }>());

export const getScrimUserStatus = createAction('[Scrim] Get Scrim User Status', props<{ scrimId: number}>());
export const getScrimUserStatusSuccess = createAction('[Scrim] Get Scrim User Status Success', props<{ userStatus: ScrimDetailsResponse }>());
export const getScrimUserStatusFailure = createAction('[Scrim] Get Scrim User Status Failure', props<{ error: any }>());

export const joinScrim = createAction('[Scrim] Join Scrim', props<{ scrimId: number }>())
export const joinScrimSuccess = createAction('[Scrim] Join Scrim Success', props<{ scrim: Scrim }>())
export const joinScrimFailure = createAction('[Scrim] Join Scrim Success', props<{ error: any }>())

export const deleteScrim = createAction('[Scrim] Delete Scrim', props<{ scrimId: number }>());
export const deleteScrimSuccess = createAction('[Scrim] Delete Scrim Success', props<{ scrimId: number }>());
export const deleteScrimFailure = createAction('[Scrim] Delete Scrim Failure', props<{ error: any }>());

export const cancelScrim = createAction('[Scrim] Cancel Scrim', props<{ scrimId: number }>());
export const cancelScrimSuccess = createAction('[Scrim] Cancel Scrim Success', props<{ scrim: Scrim }>());
export const cancelScrimFailure = createAction('[Scrim] Cancel Scrim Failure', props<{ error: any }>());

export const loadScrimHistory = createAction('[Scrim] Load Scrim History', props<{ timezone: string }>());
export const loadScrimHistorySuccess = createAction('[Scrim] Load Scrim History Success', props<{ scrims: Scrim[] }>());
export const loadScrimHistoryFailure = createAction('[Scrim] Load Scrim History Failure', props<{ error: any }>());

export const loadScrimProposals = createAction('[Scrim] Load Scrim Proposals', props<{ scrimId: number}>());
export const loadScrimProposalsSuccess = createAction('[Scrim] Load Scrim Proposals Success', props<{ scrimProposals: ScrimProposal[] }>());
export const loadScrimProposalsFailure = createAction('[Scrim] Load Scrim Proposals Failure', props<{ error: any }>());

export const sendScrimProposal = createAction('[Scrim] Send Scrim Proposal', props<{ scrimId: number, selectedPlayerIds: string[], userTimeZone: string }>())
export const sendScrimProposalSuccess = createAction('[Scrim] Send Scrim Proposal Success', props<{ scrim : Scrim }>())
export const sendScrimProposalFailure = createAction('[Scrim] Send Scrim Proposal Success', props<{ error: any }>())

export const cancelScrimProposal = createAction('[Scrim] Cancel Scrim Proposal', props<{ scrimId: number, userTimeZone: string }>())
export const cancelScrimProposalSuccess = createAction('[Scrim] Cancel Scrim Proposal Success', props<{ scrim : Scrim }>())
export const cancelScrimProposalFailure = createAction('[Scrim] Cancel Scrim Proposal Success', props<{ error: any }>())

export const acceptScrimProposal = createAction('[Scrim] Accept Scrim Proposal', props<{ proposalId: number, userTimeZone: string }>())
export const acceptScrimProposalSuccess = createAction('[Scrim] Accept Scrim Proposal Success', props<{ scrim : Scrim }>())
export const acceptScrimProposalFailure = createAction('[Scrim] Accept Scrim Proposal Success', props<{ error: any }>())

export const rejectScrimProposal = createAction('[Scrim] Reject Scrim Proposal', props<{ proposalId: number, userTimeZone: string }>())
export const rejectScrimProposalSuccess = createAction('[Scrim] Reject Scrim Proposal Success', props<{ scrim : Scrim }>())
export const rejectScrimProposalFailure = createAction('[Scrim] Reject Scrim Proposal Success', props<{ error: any }>())
