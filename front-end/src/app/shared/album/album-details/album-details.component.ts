import {
  ChangeDetectionStrategy,
  Component,
  input,
  signal,
} from '@angular/core';

import { Store } from '@ngrx/store';

import type { Album } from '../../../albums/album';
import * as playlistActions from '../../../player/store/actions';
import type { PlaylistRootState } from '../../../player/store/state';
import type { Playlist } from '../../../playlists/models';
import { PlaylistsService } from '../../../playlists/playlists.service';
import type { Song } from '../../../songs/song';
import { UiModule } from '../../../ui/ui.module';
import { AddToPlaylistModalComponent } from '../../add-to-playlist/add-to-playlist-modal.component';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [UiModule, AddToPlaylistModalComponent],
})
export class AlbumDetailsComponent {
  album = input.required<Album>();

  showAddToPlaylistModal = signal(false);
  currentSong = signal<Song | null>(null);
  playlists = signal<Playlist[]>([]);

  constructor(
    private readonly _store: Store<PlaylistRootState>,
    private readonly _playlistsService: PlaylistsService,
  ) {}

  addSongToPlaylist(songId: number) {
    this._store.dispatch(playlistActions.addToPlaylist({ songIds: [songId] }));
  }

  /**
   * Opens the modal to add a song to a playlist
   */
  openAddToPlaylistModal(song: Song) {
    this.currentSong.set(song);

    // Load playlists
    this._playlistsService.getPlaylists().subscribe((playlists) => {
      this.playlists.set(playlists);
      this.showAddToPlaylistModal.set(true);
    });
  }

  /**
   * Closes the add to playlist modal
   */
  closeAddToPlaylistModal() {
    this.showAddToPlaylistModal.set(false);
  }
}
