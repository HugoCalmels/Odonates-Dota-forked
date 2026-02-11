import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, concatMap, map, withLatestFrom} from 'rxjs/operators';
import {EMPTY, of, tap} from 'rxjs';
import {LobbySauvageActions} from './lobby-sauvage.actions';
import {LobbySauvageService} from "../../service/lobby-sauvage.service";
import {LobbySauvage} from "./lobby-sauvage.model";
import {LobbySauvageState, selectCurrentLobby} from "./lobby-sauvage.reducer";
import {select, Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {WebsocketService} from "../../service/websocket-service";


@Injectable()
export class LobbySauvageEffects {

  getAllLobbySauvage$ = createEffect(
    () => {
      return this.actions$.pipe(
      ofType(LobbySauvageActions.getAllLobbySauvage),
        concatMap( () => {
          return this.service.getAllLobbySauvage().pipe(
            map( data => LobbySauvageActions.getAllLobbySauvageSuccess({lobbySauvages: data})),
            catchError(error => of(LobbySauvageActions.createLobbySauvageFailure({ error: error })))
          )
        }))
    }
  )

  getCurrentLobby$ = createEffect(
    () => {
      return this.actions$.pipe(
      ofType(LobbySauvageActions.getCurrentLobby),
        concatMap( () => {
          return this.service.getCurrentLobby().pipe(
            map( data => LobbySauvageActions.getCurrentLobbySuccess({lobby: data})),
            catchError(error => of(LobbySauvageActions.getCurrentLobbyFailure({ error: error })))
          )
        }))
    }
  )

  createLobbySauvage$ = createEffect(
    () => {
      return this.actions$.pipe(
      ofType(LobbySauvageActions.createLobbySauvage),
        concatMap( () => {
          return this.service.createLobbySauvage().pipe(
            map( data => {
              return LobbySauvageActions.createLobbySauvageSuccess({lobbySauvage: data});
            }),
            catchError(error => of(LobbySauvageActions.createLobbySauvageFailure({ error: error })))
          )
        }))
    }
  )

  leaveLobbySauvage$ = createEffect(
    () => {
      return this.actions$.pipe(
      ofType(LobbySauvageActions.leaveLobby),
        concatMap( () => {
          return this.service.leaveLobbySauvage().pipe(
            map( data => LobbySauvageActions.leaveLobbySuccess()),
            tap(data => {
              this.webSocketService.deactivate()
            }),
            catchError(error => of(LobbySauvageActions.createLobbySauvageFailure({ error: error })))
          )
        }))
    }
  )

  joinLobbySauvage$ = createEffect(
    () => {
      return this.actions$.pipe(
      ofType(LobbySauvageActions.joinLobby),
        concatMap( (action) => {
          return this.service.joinLobbySauvage(action.lobby.id).pipe(
            map( data => LobbySauvageActions.joinLobbySuccess({lobby: data})),
            catchError(error => of(LobbySauvageActions.createLobbySauvageFailure({ error: error })))
          )
        }))
    }
  )

  updateCurrentLobby$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(LobbySauvageActions.updateCurrentLobby),
      concatMap(() => {
          return this.service.getCurrentLobby().pipe(
            map(data => LobbySauvageActions.getCurrentLobbySuccess({ lobby: data })),
            catchError(error => of(LobbySauvageActions.updateCurrentLobbyFailure({ error: error })))
          );
      })
    );
  });

  navigateToLobbyOnJoin$ = createEffect(() =>
      this.actions$.pipe(
        ofType(LobbySauvageActions.joinLobbySuccess, LobbySauvageActions.createLobbySauvageSuccess),
        tap((action ) => {
          this.router.navigate(['/lobby-sauvage/current']);
        })
      ),
    { dispatch: false }
  );

  navigateToLobbyListOnLeave$ = createEffect(() =>
      this.actions$.pipe(
        ofType(LobbySauvageActions.leaveLobbySuccess),
        tap((data) => this.lobbySauvageStore.dispatch(LobbySauvageActions.getAllLobbySauvage())),
        tap((action ) => this.router.navigate(['/lobby-sauvage']))
      ),
    { dispatch: false }
  );

  constructor(private actions$: Actions,
              private service: LobbySauvageService,
              private router: Router,
              private lobbySauvageStore: Store<LobbySauvageState>,
              private webSocketService: WebsocketService) {}
}
