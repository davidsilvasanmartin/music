import { Component } from '@angular/core';

import { Store } from '@ngrx/store';

import { Album } from '../../../../albums/album';
import * as playlistActions from '../../../store/actions';
import { PlaylistRootState } from '../../../store/state';
import { AlbumComponent } from '../../album.component';
import { AlbumControl } from '../album-control';

@Component({
  selector: 'app-playlist-replace',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Add to playlist"
    >
      <app-icon-play />
    </button>
  `,
})
export class PlaylistReplaceComponent extends AlbumControl {
  constructor(
    albumComponent: AlbumComponent,
    private readonly _store: Store<PlaylistRootState>,
  ) {
    super(albumComponent);
  }

  onClickAction(album: Album) {
    this._store.dispatch(playlistActions.reset());
    this._store.dispatch(playlistActions.addToPlaylist({ songs: album.songs }));
  }
}
