import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ElementRef,
  input,
  OnDestroy,
  output,
  signal,
  ViewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { Store } from '@ngrx/store';

import { take } from 'rxjs';

import type { Playlist } from '../../playlists/models';
import { PlaylistsService } from '../../playlists/playlists.service';
import type { Song } from '../../songs/song';
import { UiModule } from '../../ui/ui.module';

@Component({
  selector: 'app-add-to-playlist-modal',
  templateUrl: './add-to-playlist-modal.component.html',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, ReactiveFormsModule, UiModule],
})
export class AddToPlaylistModalComponent implements AfterViewInit, OnDestroy {
  song = input.required<Song>();
  playlists = input.required<Playlist[]>();
  modalClosed = output<void>();

  @ViewChild('dialog') dialogRef!: ElementRef<HTMLDialogElement>;

  showNewPlaylistForm = signal<boolean>(false);
  readonly newPlaylistForm = this._formBuilder.group({
    name: new FormControl<string>('', [Validators.required]),
    description: new FormControl<string>(''),
  });

  constructor(
    private readonly _store: Store,
    private readonly _formBuilder: FormBuilder,
    private readonly _playlistsService: PlaylistsService,
    private readonly _destroyRef: DestroyRef,
  ) {}

  readonly modalCloseListener = () => this.modalClosed.emit();

  ngAfterViewInit(): void {
    // Open the dialog modally when the view is initialized
    this.dialogRef.nativeElement.showModal();
    /**
     * Here is how closing the modal works
     * 1. Either the user presses ESC or clicks the close button and `this.closeModal()` is run
     * 2. Any of these 2 fires the native `dialog.close()` event
     * 3. The event listener below fires and emits `modalClosed`
     * 4. The parent component receives this emission
     * 5. The parent component destroys this component
     */
    this.dialogRef.nativeElement.addEventListener(
      'close',
      this.modalCloseListener,
    );

    // Optional: Handle backdrop click to close
    // Note: This is not default behavior for showModal()
    // this.dialogRef.nativeElement.addEventListener('click', (event) => {
    //   if (event.target === this.dialogRef.nativeElement) {
    //     this.closeModal();
    //   }
    // });
  }

  /**
   * Closes the modal
   */
  closeModal(): void {
    this.dialogRef.nativeElement.close();
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

  ngOnDestroy() {
    if (this.dialogRef?.nativeElement) {
      this.dialogRef.nativeElement.removeEventListener(
        'close',
        this.modalCloseListener,
      );
      if (this.dialogRef.nativeElement.open) {
        this.dialogRef.nativeElement.close();
      }
    }
  }
}
