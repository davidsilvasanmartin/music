import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { ObservableInput, of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';

import { User } from '../models';
import { AuthService } from '../services/auth.service';
import * as authActions from './actions';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(authActions.login),
      switchMap(({ username, password }) =>
        this.authService.login({ username, password }).pipe(
          map((user: User) => {
            console.log('SUCCESS Logging in', user);
            return authActions.loginSuccess({ user });
          }),
          catchError((error) => {
            console.error(error);
            return of(null) as ObservableInput<any>;
          }),
        ),
      ),
    ),
  );

  loginSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(authActions.loginSuccess),
      tap(() => this.router.navigate(['/'])),
    ),
  );

  constructor(
    private readonly actions$: Actions,
    private readonly authService: AuthService,
    private readonly router: Router,
  ) {}
}
