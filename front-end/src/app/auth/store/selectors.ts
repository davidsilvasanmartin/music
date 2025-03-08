import { createFeatureSelector, createSelector } from '@ngrx/store';

import { AuthState } from './state';

const getAuthState = createFeatureSelector<AuthState>('auth');

export const getUser = createSelector(
  getAuthState,
  (state: AuthState) => state.user,
);

/**
 * This selector may be called before the AuthModule has had the chance to be lazy-loaded.
 */
export const isAuthenticated = createSelector(
  getAuthState,
  (state: AuthState) => !!state && !!state.user && !!state.user.email,
);
