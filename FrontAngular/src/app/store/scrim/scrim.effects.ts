import { Injectable } from '@angular/core';
import { Actions, ofType, createEffect } from '@ngrx/effects';
import { mergeMap, map, catchError, tap } from 'rxjs/operators';
import { EMPTY, of } from 'rxjs';
import * as ScrimActions from './scrim.actions';
import { ScrimService } from 'src/app/service/scrim.service';
import { Router } from '@angular/router';

@Injectable()
export class ScrimEffects {

  loadScrimList$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.loadScrimList),
    mergeMap((action) => { 
      const timezone = action.timezone; 
      return this.scrimService.getScrimList(timezone).pipe(
        map(scrims => ScrimActions.loadScrimListSuccess({ scrims })),
        catchError(error => of(ScrimActions.loadScrimListFailure({ error })))
      );
    })
  ));

  loadScrimDetails$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ScrimActions.loadScrimDetails),
      mergeMap(action => {
        return this.scrimService.getFullScrim(action.scrimId, action.userTimeZone).pipe(
          map(scrim => {
            return ScrimActions.loadScrimDetailsSuccess({ scrim });
          }),
          catchError(error => {
            console.log("One error mdr")
            console.log(error)
            return of(ScrimActions.loadScrimDetailsFailure({ error }));
          })
        );
      })
    )
  );

  createScrim$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.createScrim),
    mergeMap(action => {
      const { timezone, newScrim, selectedPlayerIds } = action;
      return this.scrimService.createScrim(newScrim, timezone, selectedPlayerIds).pipe(
        map(scrim => ScrimActions.createScrimSuccess({ scrim })),
        tap(action => {
          if (action.scrim) {
            this.router.navigate([`/scrim/${action.scrim.id}`]);
          }
        }),
        catchError(() => EMPTY) 
      );
    })
  ));

  loadScrimUserStatus$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ScrimActions.getScrimUserStatus),
      mergeMap(action =>
        this.scrimService.getScrimUserStatus(action.scrimId).pipe(
          map(userStatus => ScrimActions.getScrimUserStatusSuccess({ userStatus })),
          catchError(error => of(ScrimActions.getScrimUserStatusFailure({ error })))
        )
      )
    )
  );

  joinScrim$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ScrimActions.joinScrim),
      mergeMap(action =>
        this.scrimService.joinScrim(action.scrimId).pipe(
          map(scrim => ScrimActions.joinScrimSuccess({ scrim })),
          catchError(error => of(ScrimActions.joinScrimFailure({ error })))
        )
      )
    )
  );

  deleteScrim$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ScrimActions.deleteScrim),
      mergeMap(action =>
        this.scrimService.deleteScrim(action.scrimId).pipe(
          map(() => ScrimActions.deleteScrimSuccess({ scrimId: action.scrimId })),
          catchError(error => of(ScrimActions.deleteScrimFailure({ error })))
        )
      )
    )
  );

  deleteScrimSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(ScrimActions.deleteScrimSuccess),
        tap(() => {
          this.router.navigate(['/scrims']); 
        })
      ),
    { dispatch: false } 
  );

  cancelScrim$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ScrimActions.cancelScrim),
      mergeMap(action =>
        this.scrimService.cancelScrim(action.scrimId).pipe(
          map(scrim => ScrimActions.cancelScrimSuccess({ scrim })),
          catchError(error => of(ScrimActions.cancelScrimFailure({ error })))
        )
      )
    )
  );

  loadScrimHistory$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.loadScrimHistory),
    mergeMap((action) => { 
      const timezone = action.timezone; 
      return this.scrimService.getScrimHistory(timezone).pipe(
        map(scrims => ScrimActions.loadScrimHistorySuccess({ scrims })),
        catchError(error => of(ScrimActions.loadScrimHistoryFailure({ error })))
      );
    })
  ));

  loadScrimProposals$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.loadScrimProposals),
    mergeMap(({ scrimId }) => { 
      return this.scrimService.loadProposals(scrimId).pipe(
        map(scrimProposals => ScrimActions.loadScrimProposalsSuccess({ scrimProposals })),
        catchError(error => of(ScrimActions.loadScrimProposalsFailure({ error })))
      );
    })
  ));

  sendScrimProposal$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.sendScrimProposal),
    mergeMap(({ scrimId, selectedPlayerIds, userTimeZone }) => {
      return this.scrimService.sendScrimProposal(scrimId, selectedPlayerIds, userTimeZone).pipe(
        map((scrim) => {
          return ScrimActions.sendScrimProposalSuccess({ scrim });
        }),
        catchError(error => {
          return of(ScrimActions.sendScrimProposalFailure({ error }));
        })
      );
    })
  ));

  cancelScrimProposal$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.cancelScrimProposal),
    mergeMap(({ scrimId, userTimeZone  }) => {
      return this.scrimService.cancelScrimProposal(scrimId, userTimeZone).pipe(
        map((scrim) => {
          return ScrimActions.cancelScrimProposalSuccess({ scrim });
        }),
        catchError(error => {
          return of(ScrimActions.cancelScrimProposalFailure({ error }));
        })
      );
    })
  ));

  acceptScrimProposal$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.acceptScrimProposal),
    mergeMap(({ proposalId, userTimeZone  }) => {
      return this.scrimService.acceptScrimProposal(proposalId, userTimeZone).pipe(
        map(scrim => {
          return ScrimActions.acceptScrimProposalSuccess({ scrim});
        }),
        catchError(error => {
          return of(ScrimActions.acceptScrimProposalFailure({ error }));
        })
      );
    })
  ));

  rejectScrimProposal$ = createEffect(() => this.actions$.pipe(
    ofType(ScrimActions.rejectScrimProposal),
    mergeMap(({ proposalId, userTimeZone }) => { 
      return this.scrimService.rejectScrimProposal(proposalId, userTimeZone).pipe(
        map(scrim => {
          return ScrimActions.rejectScrimProposalSuccess({ scrim });
        }),
        catchError(error => {
          return of(ScrimActions.rejectScrimProposalFailure({ error }));
        })
      );
    })
  ));;

  constructor(
    private actions$: Actions,
    private scrimService: ScrimService,
    private router: Router,
  ) {}
}