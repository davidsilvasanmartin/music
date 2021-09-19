import { Component } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Album } from './album';
import { AlbumsService } from './albums.service';

@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.scss'],
})
export class AlbumsComponent {
  albums$: Observable<Album[]>;
  apiUrl = environment.apiUrl;

  constructor(private readonly _albumsService: AlbumsService) {
    this.albums$ = this._albumsService.getAlbums();
  }
}
