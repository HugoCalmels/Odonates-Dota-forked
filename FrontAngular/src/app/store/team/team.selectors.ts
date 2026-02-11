import { createFeatureSelector, createSelector } from '@ngrx/store';
import { TeamState } from './team.reducer';
import { Team } from 'src/app/models/team.model';

export const selectTeamState = createFeatureSelector<TeamState>('team');

export const selectAllTeams = createSelector(
  selectTeamState,
  (state: TeamState) => state.teams
);

export const selectTeamsIncludingSearchQuery = createSelector(
  selectAllTeams,
  (teams: Team[], props: { query: string }) => {
    const searchQuery = props.query;
    if (!searchQuery) {
      return teams;
    }
    return teams.filter(team =>
      team.name && team.name.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }
);

export const selectVisitedTeam = createSelector(
  selectTeamState,
  (state: TeamState) => state.visitedTeam
);

export const selectCurrentUserTeam = createSelector(
  selectTeamState,
  (state: TeamState) => state.currentUserTeam
);

