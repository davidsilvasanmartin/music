import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  Inject,
  OnInit,
  Signal,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

import { take } from 'rxjs';

import { MODAL_DATA } from '../../modal/modal-data';
import { ModalWrapperComponent } from '../../modal/modal-wrapper.component';
import type { Playlist } from '../../playlists/models';
import { PlaylistsService } from '../../playlists/playlists.service';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-add-to-playlist-modal',
  templateUrl: './add-to-playlist-modal.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddToPlaylistModalComponent implements OnInit {
  song: Signal<Song>;
  playlists = signal<Playlist[]>([]);
  showNewPlaylistForm = signal<boolean>(false);
  readonly newPlaylistForm = this._formBuilder.group({
    name: new FormControl<string>('', [Validators.required]),
    description: new FormControl<string>(''),
  });

  /**
   * TODO !!!!
   *  Think about moving this component into a different directory
   *
   */
  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly _playlistsService: PlaylistsService,
    private readonly _destroyRef: DestroyRef,
    @Inject(MODAL_DATA) private readonly _modalData: { song: Song },
    private readonly _modalWrapperComponent: ModalWrapperComponent,
  ) {
    this.song = signal(this._modalData.song).asReadonly();
  }

  ngOnInit() {
    this._playlistsService.getPlaylists().subscribe((playlists) => {
      this.playlists.set(playlists);
    });
  }

  /**
   * Closes the modal
   */
  closeModal(): void {
    this._modalWrapperComponent.closeDialog();
  }

  /**
   * Shows the form to create a new playlist
   */
  showCreateNewPlaylist(): void {
    this.showNewPlaylistForm.set(true);
  }

  /**
   * Hides the form to create a new playlist
   */
  hideCreateNewPlaylist(): void {
    this.showNewPlaylistForm.set(false);
  }

  /**
   * Stub method for adding a song to an existing playlist
   * This will be implemented later
   */
  addToExistingPlaylist(existingPlaylistId: number): void {
    console.log(
      `Adding song ${this.song().id} to playlist ${existingPlaylistId}`,
    );

    // TODO save the playlist here, on success close modal
    this.closeModal();
  }

  /**
   * Stub method for creating a new playlist with the song
   * This will be implemented later
   */
  createNewPlaylist(): void {
    if (!this.newPlaylistForm.valid) {
      return;
    }

    console.log(
      `Creating new playlist "${this.newPlaylistForm.value}" with song ${this.song().id}`,
    );

    this._playlistsService
      .createPlaylist({
        name: this.newPlaylistForm.controls.name.value || '',
        description: this.newPlaylistForm.controls.description.value || '',
        items: [{ song: { id: this.song().id } }],
      })
      .pipe(take(1), takeUntilDestroyed(this._destroyRef))
      .subscribe((playlist) => {
        console.log('Created playlist', JSON.stringify(playlist, null, 2));
        this.closeModal();
      });
  }
}
