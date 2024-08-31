import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

import { Album } from '../../albums/album';

@Component({
  selector: 'app-album-dialog',
  template: `<div mat-dialog-title>
      <app-album [album]="album">
        <app-playlist-replace></app-playlist-replace>
        <app-playlist-play-album></app-playlist-play-album>
      </app-album>
    </div>
    <mat-dialog-content>
      <app-song *ngFor="let song of album.songs" [song]="song">
        <app-playlist-play></app-playlist-play>
      </app-song>
    </mat-dialog-content>`,
})
export class AlbumDialogComponent implements OnInit {
  album: Album;

  constructor(
    @Inject(MAT_DIALOG_DATA) private readonly _data: { album: Album }
  ) {
    this.album = this._data.album;
  }

  ngOnInit(): void {}
}
