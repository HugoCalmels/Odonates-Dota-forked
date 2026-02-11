import { createAction } from '@ngrx/store';

export const AuthActions = {
  setAuthenticated: createAction('[Auth] Set Authenticated'),
  setUnauthenticated: createAction('[Auth] Set Unauthenticated')
};