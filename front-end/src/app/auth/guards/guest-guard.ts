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

export const guestGuard: CanActivateFn & CanMatchFn = async () => {
  const router = inject(Router);
  const store = inject(Store);
  const isAuthenticated: boolean = await firstValueFrom(
    store.pipe(select(authSelectors.isAuthenticated)),
  );
  if (isAuthenticated) {
    console.log('REDIRECTING TO /');
    return new RedirectCommand(router.parseUrl('/'));
  }
  return true;
};
