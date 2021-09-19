import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../environments/environment';
import { Song } from './song';

@Injectable({ providedIn: 'root' })
export class SongsService {
  private readonly _url = environment.apiUrl;

  constructor(private readonly _http: HttpClient) {}

  getSongs(): Observable<Song[]> {
    return this._http.get<Song[]>(`${this._url}/songs`);
  }
}
