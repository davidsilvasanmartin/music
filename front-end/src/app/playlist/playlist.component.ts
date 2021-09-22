import { Component, OnInit } from '@angular/core';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';

import * as playerSelectors from './store/selectors';
import * as playerActions from './store/actions';
import { Song } from '../songs/song';
import { PlaylistRootState } from './store/state';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-playlist',
  templateUrl: './playlist.component.html',
})
export class PlaylistComponent implements OnInit {
  currentSong$: Observable<Song>;
  currentSongImgUrl$: Observable<string>;
  currentSongAudioUrl$: Observable<string>;
  nextSongs$: Observable<Song[]>;

  constructor(
    private readonly _store: Store<PlaylistRootState>,
    private readonly _apiService: ApiService
  ) {
    this.currentSong$ = this._store.pipe(
      select(playerSelectors.getCurrentSong)
    );
    this.currentSongImgUrl$ = this.currentSong$.pipe(
      filter<Song>(Boolean),
      map((song: Song) =>
        this._apiService.createApiUrl(`/songs/${song.id}/albumArt`)
      )
    );
    this.currentSongAudioUrl$ = this.currentSong$.pipe(
      filter<Song>(Boolean),
      map((song: Song) =>
        this._apiService.createApiUrl(`/songs/${song.id}/play`)
      )
    );
    this.nextSongs$ = this._store.pipe(select(playerSelectors.getNextSongs));
  }

  goToNextSong() {
    console.log('nEXT!');
    this._store.dispatch(playerActions.next());
  }

  ngOnInit(): void {}
}
