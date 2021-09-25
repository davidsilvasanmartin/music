import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { AlbumDialogComponent } from 'src/app/playlist/album-dialog/album-dialog.component';

import { Album } from '../../../../albums/album';
import { AlbumComponent } from '../../album.component';
import { AlbumControl } from '../album-control';

@Component({
  selector: 'app-album-peek',
  template: `
    <button mat-mini-fab color="primary" aria-label="Add to playlist">
      <mat-icon>visibility</mat-icon>
    </button>
  `,
})
export class AlbumPeekComponent extends AlbumControl {
  constructor(
    albumComponent: AlbumComponent,
    private readonly _dialog: MatDialog
  ) {
    super(albumComponent);
  }

  onClickAction(album: Album) {
    this._dialog.open(AlbumDialogComponent, { data: { album } });
  }
}
