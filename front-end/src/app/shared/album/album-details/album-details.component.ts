import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import type { Album } from '../../../albums/album';
import * as playlistActions from '../../../player/store/actions';
import type { PlaylistRootState } from '../../../player/store/state';
import { UiModule } from '../../../ui/ui.module';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [UiModule],
})
export class AlbumDetailsComponent {
  album = input.required<Album>();

  // TODO is Store ok here ??
  constructor(private readonly _store: Store<PlaylistRootState>) {}

  addSongToPlaylist(songId: number) {
    this._store.dispatch(playlistActions.addToPlaylist({ songIds: [songId] }));
  }
}
