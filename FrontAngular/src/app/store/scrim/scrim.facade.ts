import { Injectable } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { loadScrimList } from './scrim.actions';
import { Observable, filter } from 'rxjs';
import { ScrimState } from './scrim.reducer';
import { Scrim } from 'src/app/models/scrim/scrim.model';
import { selectScrims, selectCurrentScrim, selectUserStatus} from './scrim.selectors';
import { createScrim, loadScrimDetails, getScrimUserStatus, joinScrim, deleteScrim, cancelScrim, loadScrimHistory, loadScrimProposals, sendScrimProposal, cancelScrimProposal, acceptScrimProposal, rejectScrimProposal } from './scrim.actions';
import { ScrimDetailsResponse } from 'src/app/models/scrim/scrimDetailsResponse';

@Injectable({
  providedIn: 'root'
})
export class ScrimFacade {
  scrimList$: Observable<Scrim[]>; 
  scrim$: Observable<Scrim | null>;
  userStatus$: Observable<ScrimDetailsResponse | null>;

  constructor(private store: Store<ScrimState>) {
    this.scrimList$ = this.store.pipe(select(selectScrims));
    this.scrim$ = this.store.pipe(
      select(selectCurrentScrim),
      filter((scrim): scrim is Scrim => scrim !== null)
    );
    this.userStatus$ = this.store.pipe(select(selectUserStatus));
  }

  loadScrimList(timezone: string): void { 
    this.store.dispatch(loadScrimList({ timezone })); 
  }

  createScrim(newScrim: Scrim, timezone: string, selectedPlayerIds: string[]): void {
    this.store.dispatch(createScrim({ newScrim, timezone, selectedPlayerIds }));
  }

  loadScrimDetails(scrimId: number, userTimeZone: string): void {
    this.store.dispatch(loadScrimDetails({ scrimId, userTimeZone }));
  }

  getScrimUserStatus(scrimId: number): void {
    this.store.dispatch(getScrimUserStatus({ scrimId }));
  }

  joinScrim(scrimId: number) {
    this.store.dispatch(joinScrim({ scrimId }));
  }

  deleteScrim(scrimId: number) {
    this.store.dispatch(deleteScrim({ scrimId }));
  }

  cancelScrim(scrimId: number) {
    this.store.dispatch(cancelScrim({ scrimId }));
  }

  loadScrimHistory(timezone: string): void { 
    this.store.dispatch(loadScrimHistory({ timezone })); 
  }

  loadScrimProposals(scrimId: number): void { 
    this.store.dispatch(loadScrimProposals({ scrimId })); 
  }

  sendScrimProposal(scrimId: number, selectedPlayerIds: string[], userTimeZone: string): void { 
    this.store.dispatch(sendScrimProposal({ scrimId, selectedPlayerIds, userTimeZone })); 
  }

  cancelScrimProposal(scrimId: number, userTimeZone: string): void { 
    this.store.dispatch(cancelScrimProposal({ scrimId, userTimeZone })); 
  }

  acceptScrimProposal(proposalId: number, userTimeZone: string): void { 
    this.store.dispatch(acceptScrimProposal({ proposalId, userTimeZone  })); 
  }

  rejectScrimProposal(proposalId: number, userTimeZone: string): void { 
    this.store.dispatch(rejectScrimProposal({ proposalId, userTimeZone  })); 
  }
  
}