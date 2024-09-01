import { Component, input } from '@angular/core';

import { Album } from '../album';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrl: './album.component.scss',
})
export class AlbumComponent {
  album = input.required<Album>();
}
