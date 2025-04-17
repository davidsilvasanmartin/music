import {
  ChangeDetectionStrategy,
  Component,
  input,
  signal,
} from '@angular/core';
import { Params, RouterModule } from '@angular/router';

import type { Album } from '../../albums/album';
import type { Artist } from '../../artists/artist';
import { SearchMapperService } from '../../ui/search';
import { UiModule } from '../../ui/ui.module';
import { AlbumDetailsComponent } from './album-details/album-details.component';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterModule, UiModule, AlbumDetailsComponent],
})
export class AlbumComponent {
  album = input.required<Album>();
  isDetailsOpen = signal(false);

  constructor(private readonly _searchMapperService: SearchMapperService) {}

  getSearchQueryParamsForAlbumArtist(albumArtist: Artist | undefined): Params {
    if (!albumArtist) {
      return {};
    }
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'artist.name',
        condition: 'contains',
        value: albumArtist.name,
      }),
      page: 1,
    };
  }

  getSearchQueryParamsForGenre(genre: string): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'genres.name',
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
