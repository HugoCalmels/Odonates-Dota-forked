import { Injectable } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { selectIsAuthenticated } from "../../store/auth/auth.selectors";
import { AuthActions } from "../../store/auth/auth.actions";
import { UserActions } from "../../store/user/user.actions";
import { EMPTY, Observable, catchError, tap } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import { environment } from 'src/environment/environment';
import { User } from "../../models/user.model";
import { selectHasOneTeam, selectUser } from 'src/app/store/user/user.selectors';
import { TeamFacade } from 'src/app/store/team/team.facade';

const backendUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  isAuthenticated$: Observable<boolean>;
  hasOneTeam$: Observable<boolean>;
  user$: Observable<User | null>;

  constructor(private store: Store, private http: HttpClient, private teamFacade: TeamFacade) {
    this.isAuthenticated$ = this.store.pipe(select(selectIsAuthenticated));
    this.hasOneTeam$ = this.store.select(selectHasOneTeam);
    this.user$ = this.store.select(selectUser);
  }

  initializeAuthentication(): void {
    this.getCurrentUser().subscribe({
      next: (user: User) => this.handleAuthentication(user),
      error: error => this.handleError(error)
    });
  }

  getRefreshToken(): Observable<HttpResponse<void>> {
    return this.http.get<void>(`${backendUrl}/api/refreshToken`, { observe: 'response' }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${backendUrl}/user/getCurrentUser`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getCurrentUserTeam(): Observable<User> {
    return this.http.get<User>(`${backendUrl}/user/getCurrentUserTeam`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  logout(): Observable<HttpResponse<void>> {
    return this.http.get<void>(`${backendUrl}/api/logout`, { observe: 'response' }).pipe(
      catchError(error => this.handleError(error))
    );
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${backendUrl}/user/getAllUsers`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  isValidRefreshToken(): Observable<boolean> {
    return this.http.get<boolean>(`${backendUrl}/api/isValidRefreshToken`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  private handleAuthentication(user: User): void {
    this.store.dispatch(AuthActions.setAuthenticated());
    this.store.dispatch(UserActions.setUser({ user: user }));
    if (user.team) {
      this.store.dispatch(UserActions.setHasOneTeam({ hasOneTeam: true }));
      this.teamFacade.setCurrentUserTeam(user.team);
    }
  }

  private handleUnAuthentication(): void {
    this.store.dispatch(AuthActions.setUnauthenticated());
    this.store.dispatch(UserActions.clearUser());
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    this.store.dispatch(AuthActions.setUnauthenticated());
    this.store.dispatch(UserActions.clearUser());
    return EMPTY;
  }

  processLogout() {
    this.handleUnAuthentication();
    this.logout().subscribe();
  }

}
