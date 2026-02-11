import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Scrim } from '../models/scrim/scrim.model';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { environment } from 'src/environment/environment';
import { ScrimDetailsResponse } from '../models/scrim/scrimDetailsResponse';
import { ScrimProposal } from '../models/scrim/scrimProposal';

const backendUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class ScrimService {

  constructor(private http: HttpClient) {
  }

  createScrim(newScrim: Scrim, timezone: string, selectedPlayerIds: string[]): Observable<Scrim> {
    let params = new HttpParams().set('timezone', timezone);
    if (selectedPlayerIds.length > 0) {
      const playerIds = selectedPlayerIds.join(',');
      params = params.set('playerIds', playerIds);
    }
    return this.http.post<Scrim>(`${backendUrl}/scrim`, newScrim, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getScrimList(timezone: string): Observable<Scrim[]> {
    const params = new HttpParams().set('timezone', timezone);
    return this.http.get<Scrim[]>(`${backendUrl}/scrim/getAllScrims`, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getFullScrim(scrimId: number, timezone: string): Observable<Scrim> {
    const params = new HttpParams().set('timezone', timezone);
    return this.http.get<Scrim>(`${backendUrl}/scrim/scrim-details/${scrimId}`, {params}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  deleteScrim(scrimId: number): Observable<void> {
    return this.http.delete<void>(`${backendUrl}/scrim/${scrimId}`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  cancelScrim(scrimId: number): Observable<Scrim> {
    return this.http.delete<Scrim>(`${backendUrl}/scrim/cancel/${scrimId}`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  joinScrim(scrimId: number): Observable<any> {
    const joinUrl = `${backendUrl}/scrim/join-scrim/${scrimId}`;
    return this.http.post(joinUrl, {}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getScrimUserStatus(scrimId: number):Observable<ScrimDetailsResponse> {
    return this.http.get<ScrimDetailsResponse>(`${backendUrl}/scrim/getScrimUserStatus/${scrimId}`,{}).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getScrimHistory(timezone: string): Observable<Scrim[]> {
    const params = new HttpParams().set('timezone', timezone);
    return this.http.get<Scrim[]>(`${backendUrl}/scrim/getScrimHistory`, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  sendScrimProposal(scrimId: number, selectedPlayerIds: string[], userTimeZone: string): Observable<Scrim> {
    let params = new HttpParams();
    if (selectedPlayerIds.length > 0) {
      const playerIds = selectedPlayerIds.join(',');
      params = params.set('playerIds', playerIds);
    }
    if (userTimeZone) {
      params = params.set('timezone', userTimeZone);
    }
    const proposalUrl = `${backendUrl}/scrim/propose/${scrimId}`;
    return this.http.post<Scrim>(proposalUrl, {}, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  cancelScrimProposal(scrimId: number, userTimeZone: string): Observable<Scrim> {
    let params = new HttpParams();
    if (userTimeZone) {
      params = params.set('timezone', userTimeZone);
    }
    const proposalUrl = `${backendUrl}/scrim/propose/${scrimId}`;
    return this.http.delete<Scrim>(proposalUrl, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  acceptScrimProposal(proposalId: number, userTimeZone: string): Observable<Scrim> {
    let params = new HttpParams();
    if (userTimeZone) {
      params = params.set('timezone', userTimeZone);
    }
    const acceptUrl = `${backendUrl}/scrim/accept/${proposalId}`;
    return this.http.post<Scrim>(acceptUrl, {}, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  rejectScrimProposal(proposalId: number, userTimeZone: string): Observable<Scrim> {
    let params = new HttpParams();
    if (userTimeZone) {
      params = params.set('timezone', userTimeZone);
    }
    const proposalUrl = `${backendUrl}/scrim/reject/${proposalId}`;
    return this.http.post<Scrim>(proposalUrl, {}, { params }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  loadProposals(scrimId: number): Observable<ScrimProposal[]> {
    const loadProposalsUrl = `${backendUrl}/scrim/proposals/${scrimId}`;
    return this.http.get<ScrimProposal[]>(loadProposalsUrl).pipe(
      catchError(error => this.handleError(error))
    );
  }

  isTeamBiggerThanOne(): Promise<boolean> {
    return firstValueFrom(this.http.get<boolean>(`${backendUrl}/scrim/isTeamBiggerThanOne`));
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    throw error;
  }
}
