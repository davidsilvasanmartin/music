import { Component, computed, OnDestroy, Signal } from '@angular/core';
import { select, Store } from '@ngrx/store';

import * as playlistSelectors from './store/selectors';
import * as playlistActions from './store/actions';
import { Song } from '../songs/song';
import { PlaylistRootState } from './store/state';
import { ApiService } from '../api/api.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-playlist',
  templateUrl: './playlist.component.html',
})
export class PlaylistComponent implements OnDestroy {
  currentSong: Signal<Song>;
  currentSongImgUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/albumArt`),
  );
  currentSongAudioUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/play`),
  );
  nextSongs: Signal<Song[]>;

  constructor(
    private readonly _store: Store<PlaylistRootState>,
    private readonly _apiService: ApiService,
  ) {
    this.currentSong = toSignal(
      this._store.pipe(select(playlistSelectors.getCurrentSong)),
      { requireSync: true },
    );
    this.nextSongs = toSignal(
      this._store.pipe(select(playlistSelectors.getNextSongs)),
      { requireSync: true },
    );
  }

  goToNextSong() {
    console.log('nEXT!');
    this._store.dispatch(playlistActions.next());
  }

  ngOnDestroy() {
    this._store.dispatch(playlistActions.reset());
  }
}
