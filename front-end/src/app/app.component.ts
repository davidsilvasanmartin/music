import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Song } from './song';
import { SongsService } from './songs.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  songs$: Observable<Song[]>;
  apiUrl = environment.apiUrl;

  constructor(private readonly _songsService: SongsService) {
    this.songs$ = this._songsService.getSongs();
  }
}
