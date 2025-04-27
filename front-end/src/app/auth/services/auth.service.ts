import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { ApiService } from '../../shared/api/api.service';
import type { User, UserDto } from '../models';
import { UsersMapperService } from './users-mapper.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private readonly _apiService: ApiService,
    private readonly _userMapperService: UsersMapperService,
  ) {}

  login(credentials: { username: string; password: string }): Observable<User> {
    return this._apiService.post<UserDto>('/auth/login', credentials).pipe(
      // TODO remove these logs
      tap((u) => console.log('POSTED succesfully', u)),
      map((u) => this._userMapperService.fromDto(u)),
      catchError(() => {
        // TODO review flow and return the correct thing here
        return of(null as unknown as User);
      }),
    );
  }

  isLoggedIn(): Observable<User | null> {
    return this._apiService.getAndThrowError<User | null>('/auth/user').pipe(
      map((u) => (u ? this._userMapperService.fromDto(u) : null)),
      catchError((err) => {
        console.error(err);
        return of(null);
      }),
    );
  }
}
