import { Action, createReducer, on } from '@ngrx/store';

import * as authActions from './actions';
import { authInitialState, type AuthState } from './state';

const reducer = createReducer(
  authInitialState,
  on(authActions.loginSuccess, (state, { user }) => ({
    ...state,
    user,
  })),
);

export const authReducer = (state: AuthState | undefined, action: Action) =>
  reducer(state, action);
