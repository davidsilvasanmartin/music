import { Component, input } from '@angular/core';

import type { Song } from '../../songs/song';
import { ModalService } from '../../ui/modal/modal-service';
import { AddToPlaylistModalComponent } from './add-to-playlist-modal.component';

@Component({
  selector: 'app-playlist-add',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Add to playlist"
      title="Add to playlist"
      (click)="openAddToPlaylistModal()"
    >
      <app-icon-playlist-add class="size-6" />
    </button>
  `,
})
export class PlaylistAddComponent {
  song = input.required<Song>();

  constructor(private readonly _modalService: ModalService) {}

  openAddToPlaylistModal() {
    this._modalService.open({
      component: AddToPlaylistModalComponent,
      data: { song: this.song() },
    });
  }
}
