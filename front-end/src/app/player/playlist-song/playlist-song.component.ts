import { Component, Input, OnInit } from '@angular/core';
import { Song } from 'src/app/songs/song';

@Component({
  selector: 'app-playlist-song',
  templateUrl: './playlist-song.component.html',
  styleUrls: ['./playlist-song.component.scss'],
})
export class PlaylistSongComponent implements OnInit {
  @Input() song: Song;

  constructor() {}

  ngOnInit(): void {}
}
