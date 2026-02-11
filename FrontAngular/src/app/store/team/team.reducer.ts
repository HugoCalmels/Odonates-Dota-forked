import { createReducer, on } from '@ngrx/store';
import { TeamActions } from './team.actions';
import { Team } from 'src/app/models/team.model';

export interface TeamState {
  teams: Team[];
  visitedTeam: Team | null;
  currentUserTeam: Team | null;
}

const initialState: TeamState = {
  teams: [],
  visitedTeam: null,
  currentUserTeam: null
};

export const teamReducer = createReducer(
  initialState,
  on(TeamActions.SetTeams, (state, { teams }) => ({ ...state, teams: teams })),
  on(TeamActions.GetAllTeams, (state) => ({ ...state})),
  on(TeamActions.SearchTeams, (state, { query }) => ({ ...state, searchQuery: query })),
  on(TeamActions.AddTeam, (state, { team }) => ({ ...state, teams: [...state.teams, team] })),
  on(TeamActions.DeleteTeam, (state, { teamId }) => {
    return {
      ...state,
      teams: state.teams.filter(team => team.id !== teamId),
      currentUserTeam: null
    };
  }),
  on(TeamActions.SetVisitedTeam, (state, { visitedTeam }) => ({ ...state, visitedTeam })),
  on(TeamActions.SetCurrentUserTeam, (state, { currentUserTeam }) => ({ ...state, currentUserTeam })),
  on(TeamActions.AddUserToTeam, (state, { team }) => ({
    ...state,
    currentUserTeam: team,
    visitedTeam: team
  })),
  on(TeamActions.RemoveUserFromTeam, (state, { team }) => ({
    ...state,
    currentUserTeam: null,
    visitedTeam: team
  })),
  on(TeamActions.SetTeamDetails, (state, { team }) => ({ ...state, visitedTeam: team })),
);