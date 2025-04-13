import { Component, input, signal } from '@angular/core';
import { Params } from '@angular/router';

import { SearchMapperService } from '../../ui/search';
import { Album } from '../album';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
})
export class AlbumComponent {
  album = input.required<Album>();
  isDetailsOpen = signal(false);

  constructor(private readonly _searchMapperService: SearchMapperService) {}

  getSearchQueryParamsForAlbumArtist(albumArtist: string): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'artist.name',
        condition: 'contains',
        value: albumArtist,
      }),
      page: 1,
    };
  }

  getSearchQueryParamsForGenre(genre: string): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'genre.name',
        condition: 'contains',
        value: genre,
      }),
      page: 1,
    };
  }

  toggleDetails() {
    this.isDetailsOpen.set(!this.isDetailsOpen());
  }
}
