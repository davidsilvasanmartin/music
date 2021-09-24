import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';

import { Song } from '../../../../songs/song';
import { PlaylistRootState } from '../../../store/state';
import * as playlistActions from '../../../store/actions';
import { SongControl } from '../song-control';
import { SongComponent } from '../../song.component';

@Component({
  selector: 'app-playlist-play',
  templateUrl: './playlist-play.component.html',
})
export class PlaylistPlayComponent extends SongControl {
  constructor(
    songComponent: SongComponent,
    private readonly _store: Store<PlaylistRootState>
  ) {
    super(songComponent);
  }

  onClickAction(song: Song) {
    this._store.dispatch(playlistActions.addToPlaylist({ songs: [song] }));
  }
}
