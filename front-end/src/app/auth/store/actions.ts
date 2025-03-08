import { createAction, props } from '@ngrx/store';

import type { User } from '../models';

export enum AuthActionTypes {
  login = '[Auth] Login',
  loginSuccess = '[Auth] Login Success',
}

export const login = createAction(
  AuthActionTypes.login,
  props<{ username: string; password: string }>(),
);

export const loginSuccess = createAction(
  AuthActionTypes.loginSuccess,
  props<{ user: User }>(),
);
