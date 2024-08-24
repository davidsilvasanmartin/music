import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Album } from '../../../../albums/album';
import { AlbumComponent } from '../../album.component';
import { AlbumControl } from '../album-control';
import { AlbumDialogComponent } from '../../../album-dialog/album-dialog.component';

@Component({
  selector: 'app-album-peek',
  template: `
    <button mat-mini-fab color="primary" aria-label="Add to playlist">
      <app-icon-eye />
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
