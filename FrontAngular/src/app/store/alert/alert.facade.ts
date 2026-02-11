import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import * as AlertActions from './alert.actions';
import { selectAlert } from './alert.selectors';
import { BehaviorSubject, Observable } from 'rxjs';
import { AlertState } from './alert.reducer';

@Injectable({
  providedIn: 'root'
})
export class AlertFacade {
  private isVisibleSubject = new BehaviorSubject<boolean>(false);
  isVisible$: Observable<boolean> = this.isVisibleSubject.asObservable();
  alert$: Observable<AlertState>;

  constructor(private store: Store) {
    this.alert$ = this.store.select(selectAlert);
    this.alert$.subscribe(alert => {
      this.isVisibleSubject.next(alert.isVisible);
    });
  }

  setAlert(alertType: string, message: string) {
    this.store.dispatch(AlertActions.setAlert({ alertType, message }));
  }

  clearAlert() {
    this.store.dispatch(AlertActions.clearAlert());
  }
}