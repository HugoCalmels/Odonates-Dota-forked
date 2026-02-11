import { createAction, props } from '@ngrx/store';
import { Team } from 'src/app/models/team.model';

export const TeamActions = {
  GetAllTeams: createAction('[Team] Get All Teams'),
  SetTeams: createAction('[Team] Set Teams', props<{ teams: Team[] }>()),
  SearchTeams: createAction('[Team] Search Teams', props<{ query: string }>()),
  AddTeam: createAction('[Team] Add Team', props<{ team: Team }>()),
  DeleteTeam: createAction('[Team] Delete Team', props<{ teamId: string }>()),
  SetVisitedTeam: createAction('[Team] Set Visited Team', props<{ visitedTeam: Team | null }>()),
  SetCurrentUserTeam: createAction('[Team] Set Current User Team', props<{ currentUserTeam: Team | null }>()),
  AddUserToTeam: createAction('[Team] Add User To Team', props<{ team: Team }>()),
  RemoveUserFromTeam: createAction('[Team] Remove User From Team', props<{ team: Team }>()),
  GetTeamDetails: createAction(
    '[Team] Get Team Details',
    props<{ teamName: string }>() 
  ),
  SetTeamDetails: createAction('[Team] Set Team Details', props<{ team: Team }>())
};