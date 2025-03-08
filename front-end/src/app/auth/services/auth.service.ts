import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { ApiService } from '../../api/api.service';
import type { User } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private readonly http: HttpClient,
    private readonly apiService: ApiService,
  ) {}

  login(credentials: { username: string; password: string }): Observable<User> {
    console.log('POSTING');
    return this.http
      .post<User>(this.apiService.createApiUrl('/auth/login'), credentials)
      .pipe(
        tap((u) => console.log('POSTED succesfully', u)),
        catchError((err) => {
          console.error(err);
          return of(null as unknown as User);
        }),
      );
  }
}
