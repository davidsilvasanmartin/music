import { Component } from '@angular/core';

import { Store } from '@ngrx/store';

import { Song } from '../../../../songs/song';
import { PlaylistRootState } from '../../../store/state';
import * as playlistActions from '../../../store/actions';
import { SongControl } from '../song-control';
import { SongComponent } from '../../song.component';

@Component({
  selector: 'app-playlist-remove',
  template: `
    <button
      class="btn rounded-full bg-gray-300 p-1 font-bold text-gray-800 hover:bg-gray-400"
      aria-label="Remove from playlist"
    >
      <app-icon-cancel [size]="4" />
    </button>
  `,
})
export class PlaylistRemoveComponent extends SongControl {
  constructor(
    songComponent: SongComponent,
    private readonly _store: Store<PlaylistRootState>,
  ) {
    super(songComponent);
  }

  onClickAction(song: Song) {
    this._store.dispatch(playlistActions.removeFromPlaylist({ song }));
  }
}
