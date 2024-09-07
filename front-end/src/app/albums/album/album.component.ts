import { Component, input } from '@angular/core';

import { Album } from '../album';

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
})
export class AlbumComponent {
  album = input.required<Album>();
}
