import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';
import { Album } from '../album';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
})
export class AlbumDetailsComponent {
  album = input.required<Album>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  addSongToPlaylist(songId: number) {
    this._store.dispatch(playlistActions.addToPlaylist({ songIds: [songId] }));
  }
}
