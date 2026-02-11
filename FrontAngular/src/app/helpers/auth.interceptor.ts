import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError, switchMap } from "rxjs";
import { AuthService } from "../service/auth/auth.service";
import { Store } from "@ngrx/store";
import { AuthActions } from "../store/auth/auth.actions";
import * as AlertActions from "../store/alert/alert.actions";
import { UserActions } from "../store/user/user.actions";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private store: Store, private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authReq = req.clone({ withCredentials: true });

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          // Ne pas loguer les erreurs 401 et 403 directement
          return this.handleAuthError(req, next);
        } else if (error.status === 400 && error.error?.error?.includes('MaxActiveScrimException')) {
          this.handleMaxScrimException(error);
          return throwError(() => error);
        } else if (error.status === 400 && error.error?.message.includes('Incorrect Team password')) {
          this.handleAlert('error', 'Incorrect Team password.');
          return throwError(() => error);
        } else if (error.status === 400 && error.error?.message.includes('Scrim not found')) {
          this.handleAlert('error', 'This scrim does not exist or has been deleted.');
          return throwError(() => error);
        } else if (error.status === 400 && error.error?.message.includes('Team not found')) {
          this.handleAlert('error', 'This team does not exist or has been deleted.');
          return throwError(() => error);
        }
        // Gestion des erreurs non liées à l'authentification
        return throwError(() => error);
      })
    );
  }

  private handleAuthError(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.authService.isValidRefreshToken().pipe(
      switchMap((isValid: boolean) => {
        if (isValid) {
          return this.authService.getRefreshToken().pipe(
            switchMap(() => {
              const authReq = req.clone({ withCredentials: true });
              console.clear(); // Effacer la console ici avant de renvoyer la requête
              return next.handle(authReq);
            }),
            catchError(refreshError => {
              this.handleAuthFailure();
              return throwError(() => refreshError);
            })
          );
        } else {
          this.handleAuthFailure();
          return throwError(() => new Error('Refresh token not valid!'));
        }
      })
    );
  }

  private handleAuthFailure(): void {
    this.store.dispatch(AuthActions.setUnauthenticated());
    this.store.dispatch(UserActions.clearUser());
  }

  private handleMaxScrimException(error: HttpErrorResponse): void {
    this.handleAlert('error', 'You cannot create more than 10 active Scrims.');
  }

  private handleAlert(alertType: string, message: string): void {
    this.store.dispatch(AlertActions.setAlert({ alertType, message }));
  }
}