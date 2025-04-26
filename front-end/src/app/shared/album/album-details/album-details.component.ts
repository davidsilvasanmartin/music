import {
  ChangeDetectionStrategy,
  Component,
  input,
  signal,
} from '@angular/core';

import type { Album } from '../../../albums/album';
import type { Playlist } from '../../../playlists/models';
import { PlaylistsService } from '../../../playlists/playlists.service';
import type { Song } from '../../../songs/song';
import { PlaylistComponentsModule } from '../../../ui/playlist-components/playlist-components.module';
import { UiModule } from '../../../ui/ui.module';
import { AddToPlaylistModalComponent } from '../../add-to-playlist/add-to-playlist-modal.component';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [UiModule, AddToPlaylistModalComponent, PlaylistComponentsModule],
})
export class AlbumDetailsComponent {
  album = input.required<Album>();

  showAddToPlaylistModal = signal(false);
  currentSong = signal<Song | null>(null);
  playlists = signal<Playlist[]>([]);

  constructor(private readonly _playlistsService: PlaylistsService) {}

  /**
   * Opens the modal to add a song to a playlist
   *
   * TODO write this functionality in the specific component !
   */
  openAddToPlaylistModal(song: Song) {
    this.currentSong.set(song);

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
