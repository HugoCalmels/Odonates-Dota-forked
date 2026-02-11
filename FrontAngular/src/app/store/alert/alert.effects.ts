import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import * as AlertActions from './alert.actions';

@Injectable()
export class AlertEffects {

  setAlert$ = createEffect(() => this.actions$.pipe(
    ofType(AlertActions.setAlert),
  ), { dispatch: false });

  constructor(private actions$: Actions) {}
}