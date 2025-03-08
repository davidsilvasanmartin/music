import { inject } from '@angular/core';
import {
  CanActivateFn,
  CanMatchFn,
  RedirectCommand,
  Router,
} from '@angular/router';

import { select, Store } from '@ngrx/store';

import { firstValueFrom } from 'rxjs';

import * as authSelectors from '../store/selectors';

export const authenticationGuard: CanActivateFn & CanMatchFn = async (
  route,
  state,
) => {
  const router = inject(Router);
  const store = inject(Store);
  console.log(route, state);
  const isAuthenticated: boolean = await firstValueFrom(
    store.pipe(select(authSelectors.isAuthenticated)),
  );
  if (isAuthenticated) {
    return true;
  }
  console.log('REDIRECTING TO LOGIN');
  return new RedirectCommand(router.parseUrl('/auth/login'));
};
