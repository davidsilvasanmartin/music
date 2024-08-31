import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Album } from '../../../../albums/album';
import { AlbumDialogComponent } from '../../../album-dialog/album-dialog.component';
import { AlbumComponent } from '../../album.component';
import { AlbumControl } from '../album-control';

@Component({
  selector: 'app-album-peek',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Add to playlist"
    >
      <app-icon-eye [size]="4" />
    </button>
  `,
})
export class AlbumPeekComponent extends AlbumControl {
  constructor(
    albumComponent: AlbumComponent,
    private readonly _dialog: MatDialog,
  ) {
    super(albumComponent);
  }

  onClickAction(album: Album) {
    this._dialog.open(AlbumDialogComponent, { data: { album } });
  }
}
