import { Component, input } from '@angular/core';
import { Params } from '@angular/router';

import { SearchMapperService } from '../../ui/search';
import { Album } from '../album';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
})
export class AlbumComponent {
  album = input.required<Album>();

  constructor(private readonly _searchMapperService: SearchMapperService) {}

  getSearchQueryParamsForAlbumArtist(albumArtist: string): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'albumArtist',
        condition: 'contains',
        value: albumArtist,
      }),
      page: 1,
    };
  }

  getSearchQueryParamsForGenre(genre: string): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'genre',
        condition: 'contains',
        value: genre,
      }),
      page: 1,
    };
  }
}
