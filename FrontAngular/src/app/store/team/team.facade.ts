import { Injectable } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from '../../app.state';
import { selectAllTeams } from './team.selectors'; // Importez le sélecteur
import { Team } from 'src/app/models/team.model';
import { TeamActions } from './team.actions';
import { TeamService } from 'src/app/service/team.service';

@Injectable({
  providedIn: 'root'
})
export class TeamFacade {
  constructor(private store: Store<AppState>, private teamService: TeamService) {
  }

  getAllTeams(): Observable<Team[]> {
    this.store.dispatch(TeamActions.GetAllTeams());
    return this.store.pipe(select(selectAllTeams));
  }

  selectAllTeams$(): Observable<Team[]> {
    return this.store.pipe(select(selectAllTeams));
  }

  setVisitedTeam(visitedTeam: Team | null): void {
    this.store.dispatch(TeamActions.SetVisitedTeam({ visitedTeam }));
  }
  
  setCurrentUserTeam(currentUserTeam: Team | null): void {
    this.store.dispatch(TeamActions.SetCurrentUserTeam({ currentUserTeam }));
  }

  checkTeamNameExists(teamName: string): Observable<boolean> {
    return this.teamService.checkTeamNameExists(teamName);
  }

  addUserToTeam(team: Team): void {
    this.store.dispatch(TeamActions.AddUserToTeam({ team }));
  }

  removeUserFromTeam(team: Team): void {
    this.store.dispatch(TeamActions.RemoveUserFromTeam({ team }));
  }
  searchTeams(query: string): Observable<Team[]> {
    this.store.dispatch(TeamActions.SearchTeams({ query }));
    return this.store.pipe(select(selectAllTeams));
  }


}