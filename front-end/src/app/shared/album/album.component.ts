import {
  ChangeDetectionStrategy,
  Component,
  input,
  signal,
} from '@angular/core';
import { Params, RouterModule } from '@angular/router';

import type { Album } from '../../albums/album';
import type { Artist } from '../../artists/artist';
import { IconsModule } from '../../ui/icons/icons.module';
import { SearchMapperService } from '../../ui/search';
import { BadgesModule } from '../badges/badges.module';
import { QueueComponentsModule } from '../queue-components/queue-components.module';
import { AlbumDetailsComponent } from './album-details/album-details.component';

/**
 * This component is used in different modules.
 *
 * TODO
 *  - The routing and query parameters is specific for the AlbumsComponent. We need to add
 *    functionality so this component can be used in other modules.
 */
@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    RouterModule,
    AlbumDetailsComponent,
    BadgesModule,
    QueueComponentsModule,
    IconsModule,
  ],
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

  getSearchQueryParamsForYear(year: number): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'year',
        condition: 'contains',
        value: year.toString(),
      }),
      page: 1,
    };
  }

  toggleDetails() {
    this.isDetailsOpen.set(!this.isDetailsOpen());
  }
}
