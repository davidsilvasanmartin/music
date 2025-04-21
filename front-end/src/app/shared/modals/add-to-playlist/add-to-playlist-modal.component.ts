import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Store } from '@ngrx/store';

import type { Playlist } from '../../../playlists/models';
import type { Song } from '../../../songs/song';
import { UiModule } from '../../../ui/ui.module';

@Component({
  selector: 'app-add-to-playlist-modal',
  templateUrl: './add-to-playlist-modal.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, UiModule],
})
export class AddToPlaylistModalComponent {
  @Input() song!: Song;
  @Input() playlists: Playlist[] = [];
  @Output() modalClosed = new EventEmitter<void>();

  showNewPlaylistForm = false;
  newPlaylistName = '';
  newPlaylistDescription = '';
  selectedPlaylistId: number | null = null;

  constructor(private readonly _store: Store) {}

  /**
   * Closes the modal
   */
  closeModal(): void {
    this.modalClosed.emit();
  }

  /**
   * Shows the form to create a new playlist
   */
  showCreateNewPlaylist(): void {
    this.showNewPlaylistForm = true;
    this.selectedPlaylistId = null;
  }

  /**
   * Hides the form to create a new playlist
   */
  hideCreateNewPlaylist(): void {
    this.showNewPlaylistForm = false;
    this.newPlaylistName = '';
    this.newPlaylistDescription = '';
  }

  /**
   * Stub method for adding a song to an existing playlist
   * This will be implemented later
   */
  addToExistingPlaylist(): void {
    if (this.selectedPlaylistId === null) {
      return;
    }

    // Stub: This will be implemented later
    console.log(
      `Adding song ${this.song.id} to playlist ${this.selectedPlaylistId}`,
    );

    this.closeModal();
  }

  /**
   * Stub method for creating a new playlist with the song
   * This will be implemented later
   */
  createNewPlaylist(): void {
    if (!this.newPlaylistName.trim()) {
      return;
    }

    // Stub: This will be implemented later
    console.log(
      `Creating new playlist "${this.newPlaylistName}" with song ${this.song.id}`,
    );

    this.closeModal();
  }
}
