import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {LobbySauvage} from "../store/lobby-sauvage/lobby-sauvage.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environment/environment";
const backendUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class LobbySauvageService {

  constructor(private http: HttpClient) { }

  createLobbySauvage(): Observable<LobbySauvage> {
    return this.http.post<LobbySauvage>(`${backendUrl}/lobby-sauvage`, {});
  }

  getAllLobbySauvage(): Observable<LobbySauvage[]> {
    return this.http.get<LobbySauvage[]>(`${backendUrl}/lobby-sauvage`);
  }

  joinLobbySauvage(id: number): Observable<LobbySauvage> {
    return this.http.post<LobbySauvage>(`${backendUrl}/lobby-sauvage/join/` + id, {});
  }

  getCurrentLobby(): Observable<LobbySauvage> {
    return this.http.get<LobbySauvage>(`${backendUrl}/lobby-sauvage/current`);
  }

  leaveLobbySauvage(): Observable<any> {
    return this.http.post(`${backendUrl}/lobby-sauvage/leave`, {}, { withCredentials: true });
  }
}

