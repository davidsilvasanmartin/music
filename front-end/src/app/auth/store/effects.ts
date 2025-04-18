import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';

import { User } from '../models';
import { AuthService } from '../services/auth.service';
import * as authActions from './actions';

@Injectable()
export class AuthEffects {
  login$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(authActions.login),
        switchMap(({ username, password, redirectTo }) =>
          this.authService.login({ username, password }).pipe(
            map((user: User) => {
              console.log('SUCCESS Logging in', user);
              return authActions.loginSuccess({ user, redirectTo });
            }),
            catchError((error) => {
              console.error(error);
              return throwError(() => error);
            }),
          ),
        ),
      ),
    // We are rethrowing the error mostly because typescript complains about the return type.
    // In any case, we don't need Ngrx to handle the error for us
    { useEffectsErrorHandler: false },
  );

  loginSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(authActions.loginSuccess),
        tap(({ redirectTo }) => {
          const target = redirectTo ? decodeURIComponent(redirectTo) : '/';
          this.router.navigateByUrl(target);
        }),
      ),
    { dispatch: false },
  );

  constructor(
    private readonly actions$: Actions,
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}
}
