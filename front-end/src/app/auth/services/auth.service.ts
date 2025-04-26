import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ApiService } from '../../shared/api/api.service';
import type { User } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private readonly http: HttpClient,
    private readonly apiService: ApiService,
  ) {}

  login(credentials: { username: string; password: string }): Observable<User> {
    return (
      this.http
        // TODO UserDto, mapping, ...
        .post<User>(this.apiService.createApiUrl('/auth/login'), credentials)
        .pipe(
          tap((u) => console.log('POSTED succesfully', u)),
          catchError((err) => {
            console.error(err);
            return of(null as unknown as User);
          }),
        )
    );
  }

  isLoggedIn(): Observable<User | null> {
    // TODO UserDto, mapping, ...
    return this.http
      .get<User | null>(this.apiService.createApiUrl('/auth/user'))
      .pipe(
        catchError((err) => {
          console.error(err);
          return of(null);
        }),
      );
  }
}
