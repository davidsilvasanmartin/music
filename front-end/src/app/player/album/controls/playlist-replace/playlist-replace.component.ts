import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import { Song } from '../../../../songs/song';
import * as playlistActions from '../../../store/actions';
import { PlaylistRootState } from '../../../store/state';

@Component({
  selector: 'app-playlist-replace',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Add to playlist"
      (click)="replacePlaylist()"
    >
      <app-icon-play />
    </button>
  `,
})
export class PlaylistReplaceComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  replacePlaylist() {
    this._store.dispatch(playlistActions.reset());
    this._store.dispatch(
      playlistActions.addToPlaylist({ songs: this.songs() }),
    );
  }
}
