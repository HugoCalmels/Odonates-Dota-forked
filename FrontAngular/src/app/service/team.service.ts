import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environment/environment';
import { Team } from '../models/team.model';

const backendUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private http: HttpClient) {
  }

  createTeam(teamData: FormData): Observable<Team> {
    return this.http.post<Team>(`${backendUrl}/team`, teamData).pipe(
      catchError(error => this.handleError(error))
    );
  }

  editTeam(teamData: FormData): Observable<Team> {
    return this.http.put<Team>(`${backendUrl}/team`, teamData).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getAllTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(`${backendUrl}/team/getAllTeams`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    throw error;
  }

  getFullTeamByName(teamName: string | null): Observable<Team> {
    return this.http.get<Team>(`${backendUrl}/team/team-details/` + teamName);
  }

  joinTeam(password: string, team: Team | null | undefined): Observable<Team> {
    const payload = { password: password, teamId: team?.id };
    return this.http.post<Team>(`${backendUrl}/team/joinTeam`, payload);
  }

  deleteTeam(): Observable<Team> {
    return this.http.delete<Team>(`${backendUrl}/team`);
  }

  getTeamPassword(): Observable<string> {
    return this.http.get(`${backendUrl}/team/getTeamPassword`, { responseType: 'text' }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  leaveTeam(): Observable<Team> {
    return this.http.delete<Team>(`${backendUrl}/team/leaveTeam`);
  }

  kickPlayer(steamId: string | null | undefined): Observable<Team> {
    const payload = {steamId: steamId };
    return this.http.post<Team>(`${backendUrl}/team/kickPlayer`, payload);
  }
  
  searchTeams(query: string): Observable<Team[]> {
    return this.http.post<Team[]>(`${backendUrl}/team/search`, { searchQuery : query});
  }

  checkTeamNameExists(teamName: string): Observable<boolean> {
    return this.http.get<boolean>(`${backendUrl}/team/existsByName/${teamName}`);
  }

  isUserTeamAdmin(teamId: string | null): Observable<boolean> {
    return this.http.get<boolean>(`${backendUrl}/team/isUserTeamAdmin/${teamId}`);
  }
  isUserTeamPlayer(teamId: string | null): Observable<boolean> {
    return this.http.get<boolean>(`${backendUrl}/team/isUserTeamPlayer/${teamId}`);
  }

  getNumberScrimsPlayed(teamId: string | null | undefined): Observable<number> {
    return this.http.get<number>(`${backendUrl}/scrim/getNumberScrimsPlayed/${teamId}`);
  }
}

