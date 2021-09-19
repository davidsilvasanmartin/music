import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { ApiService } from '../api.service';
import { Album } from './album';

@Injectable({
  providedIn: 'root',
})
export class AlbumsService {
  private readonly _url = environment.apiUrl;

  constructor(
    private readonly _http: HttpClient,
    private readonly _apiService: ApiService
  ) {}

  getAlbums(): Observable<Album[]> {
    return this._http.get<Album[]>(this._apiService.createApiUrl('albums'));
  }
}
