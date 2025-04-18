import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';

import { select, Store } from '@ngrx/store';

import { firstValueFrom } from 'rxjs';

import * as authSelectors from '../store/selectors';

export const authenticationGuard: CanActivateFn = async (route, state) => {
  const router = inject(Router);
  const store = inject(Store);
  const isAuthenticated: boolean = await firstValueFrom(
    store.pipe(select(authSelectors.isAuthenticated)),
  );

  if (isAuthenticated) {
    return true;
  }

  // Redirect to login, appending the originally requested URL
  const redirectUrl = `/auth/login?redirectTo=${encodeURIComponent(state.url)}`;
  return new RedirectCommand(router.parseUrl(redirectUrl));
};
