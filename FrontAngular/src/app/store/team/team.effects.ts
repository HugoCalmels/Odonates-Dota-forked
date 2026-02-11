import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { mergeMap, map, tap, ignoreElements } from 'rxjs/operators';
import { TeamActions } from './team.actions';
import { TeamService } from 'src/app/service/team.service';

@Injectable()
export class TeamEffects {

  constructor(private actions$: Actions, private teamService: TeamService) { }

  selectAllTeams$ = createEffect(() => this.actions$.pipe(
    ofType(TeamActions.GetAllTeams),
    mergeMap(() => this.teamService.getAllTeams().pipe(
      map(teams => TeamActions.SetTeams({ teams }))
    ))
  ));

  getTeamDetails$ = createEffect(() => this.actions$.pipe(
    ofType(TeamActions.GetTeamDetails), 
    mergeMap((action) => this.teamService.getFullTeamByName(action.teamName).pipe(
      map(teamDetails => TeamActions.SetTeamDetails({team: teamDetails})) 
    ))
  ));

  searchTeams$ = createEffect(() => this.actions$.pipe(
    ofType(TeamActions.SearchTeams),
    mergeMap((action) => this.teamService.searchTeams(action.query).pipe(
      map(teams => TeamActions.SetTeams({ teams }))
    ))
  ));

}