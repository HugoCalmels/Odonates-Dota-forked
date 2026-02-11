import { createAction, props } from '@ngrx/store';

export const setAlert = createAction(
  '[Alert] Set Alert',
  props<{ alertType: string, message: string }>()
);

export const clearAlert = createAction('[Alert] Clear Alert');